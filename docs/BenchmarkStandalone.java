import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.*;

/**
 * Réplica fiel (mesma lógica, sem anotações Spring) dos arquivos:
 *   ordenacao/prioridadeutil.java
 *   ordenacao/geradordadossinteticosservice.java
 *   ordenacao/ordenacaorequisicaoservice.java
 *
 * Objetivo: (1) provar que sequencial, threads e virtual threads devolvem
 * exatamente a mesma resposta; (2) gerar números de referência aqui no
 * sandbox (que tem 1 núcleo só — por isso NÃO representam o speedup real
 * de uma máquina multi-core; servem só de validação de que o código roda
 * de ponta a ponta sem erro).
 */
public class BenchmarkStandalone {

    record Requisicao(long id, String prioridade) {}

    static int rank(String prioridade) {
        String normalizado = Normalizer.normalize(prioridade.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return switch (normalizado) {
            case "alta", "urgente", "critica" -> 0;
            case "media" -> 1;
            case "baixa" -> 2;
            default -> 3;
        };
    }

    static final Comparator<Requisicao> COMPARADOR =
            Comparator.comparingInt((Requisicao r) -> rank(r.prioridade())).thenComparingLong(Requisicao::id);

    static List<Requisicao> gerar(int quantidade) {
        Random random = new Random(42L);
        String[] prioridades = {"alta", "media", "baixa"};
        List<Requisicao> lista = new ArrayList<>(quantidade);
        for (long id = 1; id <= quantidade; id++) {
            lista.add(new Requisicao(id, prioridades[random.nextInt(prioridades.length)]));
        }
        return lista;
    }

    static List<Requisicao> ordenarSequencial(List<Requisicao> dados) {
        List<Requisicao> copia = new ArrayList<>(dados);
        copia.sort(COMPARADOR);
        return copia;
    }

    static List<Requisicao> ordenarParticionado(List<Requisicao> dados, int nFatias, ExecutorService executor) throws Exception {
        int n = dados.size();
        int tamanhoFatia = (int) Math.ceil((double) n / nFatias);
        List<List<Requisicao>> fatias = new ArrayList<>();
        for (int inicio = 0; inicio < n; inicio += tamanhoFatia) {
            fatias.add(new ArrayList<>(dados.subList(inicio, Math.min(inicio + tamanhoFatia, n))));
        }
        List<Future<List<Requisicao>>> futuros = new ArrayList<>();
        for (List<Requisicao> fatia : fatias) {
            futuros.add(executor.submit(() -> { fatia.sort(COMPARADOR); return fatia; }));
        }
        List<List<Requisicao>> fatiasOrdenadas = new ArrayList<>();
        for (Future<List<Requisicao>> f : futuros) fatiasOrdenadas.add(f.get());
        return mesclarKVias(fatiasOrdenadas, n);
    }

    static List<Requisicao> mesclarKVias(List<List<Requisicao>> fatiasOrdenadas, int total) {
        record Apontador(Requisicao item, int fatia, int indice) {}
        PriorityQueue<Apontador> heap = new PriorityQueue<>(Comparator.comparing(Apontador::item, COMPARADOR));
        for (int i = 0; i < fatiasOrdenadas.size(); i++)
            if (!fatiasOrdenadas.get(i).isEmpty()) heap.add(new Apontador(fatiasOrdenadas.get(i).get(0), i, 0));
        List<Requisicao> resultado = new ArrayList<>(total);
        while (!heap.isEmpty()) {
            Apontador atual = heap.poll();
            resultado.add(atual.item());
            List<Requisicao> fatia = fatiasOrdenadas.get(atual.fatia());
            int prox = atual.indice() + 1;
            if (prox < fatia.size()) heap.add(new Apontador(fatia.get(prox), atual.fatia(), prox));
        }
        return resultado;
    }

    static List<Long> assinatura(List<Requisicao> l) { return l.stream().map(Requisicao::id).toList(); }

    public static void main(String[] args) throws Exception {
        System.out.println("Nucleos disponiveis neste sandbox: " + Runtime.getRuntime().availableProcessors());
        System.out.println();
        System.out.printf("%-10s | %-10s | %-8s | %-12s | %-8s | %s%n",
                "qtd", "versao", "threads", "tempoMs", "speedup", "identico?");
        System.out.println("-".repeat(70));

        int[] quantidades = {100_000, 1_000_000};
        int[] threadsList = {2, 4, 8};

        for (int quantidade : quantidades) {
            List<Requisicao> dados = gerar(quantidade);

            long t0 = System.nanoTime();
            List<Requisicao> resSeq = ordenarSequencial(dados);
            double tempoSeqMs = (System.nanoTime() - t0) / 1_000_000.0;
            List<Long> assinaturaSeq = assinatura(resSeq);
            System.out.printf("%-10d | %-10s | %-8d | %-12.2f | %-8.2f | %s%n",
                    quantidade, "sequencial", 1, tempoSeqMs, 1.0, true);

            for (int nThreads : threadsList) {
                ExecutorService pool = Executors.newFixedThreadPool(nThreads);
                long t1 = System.nanoTime();
                List<Requisicao> resPar = ordenarParticionado(dados, nThreads, pool);
                double tempoParMs = (System.nanoTime() - t1) / 1_000_000.0;
                pool.shutdown();
                boolean igual = assinaturaSeq.equals(assinatura(resPar));
                System.out.printf("%-10d | %-10s | %-8d | %-12.2f | %-8.2f | %s%n",
                        quantidade, "threads", nThreads, tempoParMs, tempoSeqMs / tempoParMs, igual);
            }

            for (int nFatias : threadsList) {
                try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
                    long t2 = System.nanoTime();
                    List<Requisicao> resVirt = ordenarParticionado(dados, nFatias, pool);
                    double tempoVirtMs = (System.nanoTime() - t2) / 1_000_000.0;
                    boolean igual = assinaturaSeq.equals(assinatura(resVirt));
                    System.out.printf("%-10d | %-10s | %-8d | %-12.2f | %-8.2f | %s%n",
                            quantidade, "virtual", nFatias, tempoVirtMs, tempoSeqMs / tempoVirtMs, igual);
                }
            }
            System.out.println();
        }
    }
}
