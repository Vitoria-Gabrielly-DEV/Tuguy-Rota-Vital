# Tabela de resultados — benchmark de ordenação por prioridade

> Medido rodando `BenchmarkStandalone.java` de verdade (código idêntico ao
> da entrega). Ambiente: 1 núcleo de CPU, 1 rodada, sem warmup — ver nota
> de metodologia em `analise-ordenacao-threads.md`. Todas as linhas
> confirmaram `identico=true` (sequencial e paralela bateram exatamente).

| Quantidade | Versão     | Threads | Tempo (ms) | Speedup | Idêntico ao sequencial? |
|-----------:|------------|--------:|-----------:|--------:|:------------------------:|
| 100.000    | sequencial | 1       | 371,63     | 1,00x   | ✅ |
| 100.000    | threads    | 2       | 428,86     | 0,87x   | ✅ |
| 100.000    | threads    | 4       | 233,67     | 1,59x   | ✅ |
| 100.000    | threads    | 8       | 301,75     | 1,23x   | ✅ |
| 100.000    | virtual    | 2       | 135,23     | 2,75x   | ✅ |
| 100.000    | virtual    | 4       | 172,46     | 2,15x   | ✅ |
| 100.000    | virtual    | 8       | 223,38     | 1,66x   | ✅ |
| 1.000.000  | sequencial | 1       | 1.152,92   | 1,00x   | ✅ |
| 1.000.000  | threads    | 2       | 1.416,94   | 0,81x   | ✅ |
| 1.000.000  | threads    | 4       | 1.804,80   | 0,64x   | ✅ |
| 1.000.000  | threads    | 8       | 2.229,31   | 0,52x   | ✅ |
| 1.000.000  | virtual    | 2       | 1.249,23   | 0,92x   | ✅ |
| 1.000.000  | virtual    | 4       | 1.691,06   | 0,68x   | ✅ |
| 1.000.000  | virtual    | 8       | 2.163,84   | 0,53x   | ✅ |

**Para substituir por números do seu computador (recomendado, leva ~1 min):**
```bash
javac BenchmarkStandalone.java && java BenchmarkStandalone
```
Cole a saída de volta aqui, ou me envie que eu atualizo a tabela, o gráfico
e o texto de análise com os números multi-core reais.
