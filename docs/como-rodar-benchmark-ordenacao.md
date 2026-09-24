# Como rodar o benchmark de ordenação por prioridade

## 1. Subir a aplicação
```bash
cd back-end/tuguy
./mvnw spring-boot:run
```
Aguarde o log `Started TuguyApplication`.

## 2. Rodar a tabela completa de benchmark (gera a tabela do relatório)
Em outro terminal:
```bash
curl "http://localhost:8080/api/v1/requisicoes/benchmark?quantidades=100000,1000000&threads=2,4,8&incluirVirtual=true" | jq .
```
- `quantidades`: tamanhos do histórico simulado (100 mil e 1 milhão, como pede o roteiro).
- `threads`: valores de thread a comparar (2, 4 e 8, como pede o roteiro).
- `incluirVirtual=true`: adiciona as linhas com Virtual Threads (Java 21) — item opcional de destaque.

A resposta é uma lista de linhas, uma por combinação (quantidade × versão × threads), já com:
- `tempoMs`
- `speedup` (tempoSequencial / tempoDaVersao)
- `resultadoIdenticoAoSequencial` (prova de que sequencial e paralela devolvem a mesma resposta)

## 3. Transformar em tabela + gráfico
1. Copie o JSON da resposta.
2. Cole numa planilha (Excel/Sheets) ou peça para o Claude gerar a tabela/gráfico a partir dos números reais — **os tempos têm que ser os medidos na sua máquina**, ninguém pode inventar esses números.
3. Gráfico sugerido: eixo X = nº de threads (1, 2, 4, 8), eixo Y = tempoMs, uma linha por quantidade (100 mil e 1 milhão).

## 4. Rodar uma única combinação (para testar rápido)
```bash
curl "http://localhost:8080/api/v1/requisicoes/ordenar?quantidade=100000&versao=sequencial"
curl "http://localhost:8080/api/v1/requisicoes/ordenar?quantidade=100000&versao=threads&threads=4"
curl "http://localhost:8080/api/v1/requisicoes/ordenar?quantidade=100000&versao=virtual&threads=8"
```

## 5. Rodar os testes automatizados que comprovam a equivalência
Se preferir provar a igualdade também via teste (além do `resultadoIdenticoAoSequencial` do benchmark), adicione um teste JUnit chamando `ordenarSequencial` e `ordenarComThreads` para o mesmo dataset e comparando `ordenacaorequisicaoservice.assinatura(...)`.
