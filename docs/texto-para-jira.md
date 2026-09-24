# Texto pronto para colar no Jira (card PI3E3 — SO, Unidade 1, "Threads em processo real")

## Descrição / comentário de entrega

Operação escolhida: ordenação das requisições hospitalares por prioridade
(camada de aplicação, endpoint REST versionado).

- Serviço real (não script solto): `ordenacaorequisicaoservice.java`,
  exposto via `ordenacaocontroller.java` em `/api/v1/requisicoes/ordenar`
  e `/api/v1/requisicoes/benchmark`.
- Big-O sequencial: O(n log n). Paralela: O((n/k) log(n/k)) por thread +
  merge k-vias O(n log k) na agregação final.
- Dados particionados em fatias independentes, uma por thread; agregação
  (merge) só no final.
- Sequencial, com threads (2/4/8) e com virtual threads (Java 21, bônus)
  testadas — todas devolveram exatamente a mesma resposta (verificado por
  assinatura da ordem final, ver `assinatura()` no serviço).
- Tabela e gráfico de tempos: `docs/tabela-resultados-ordenacao.md`.
- Análise (15–20 linhas, Big-O, threads, Amdahl, Mesa do DJ, evolução de
  arquitetura): `docs/analise-ordenacao-threads.md`.
- Como reproduzir: `docs/como-rodar-benchmark-ordenacao.md`.

## Anexos/links a colar no card
- PR/commit no GitHub: **[colar link do PR aqui depois de dar push]**
- `docs/analise-ordenacao-threads.md`
- `docs/tabela-resultados-ordenacao.md`
- `docs/como-rodar-benchmark-ordenacao.md`

## Checklist de movimentação
- [ ] Mover o card de "A Fazer"/"Em andamento" para "Em revisão" (ou
      "Concluído", conforme o fluxo do board PI3E3).
- [ ] Colar os links acima no card.
- [ ] Marcar o item no README (já feito nesta entrega).
