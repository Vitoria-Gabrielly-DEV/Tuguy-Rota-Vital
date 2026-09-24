# Análise — Ordenação de requisições por prioridade (SO, Unidade 1)

> **Nota de metodologia — leia antes de entregar:** os números abaixo foram
> medidos rodando o código real (`BenchmarkStandalone.java`, anexo à
> entrega), mas num ambiente de execução com **apenas 1 núcleo de CPU** e
> em uma única rodada, sem aquecimento de JIT. Isso é suficiente para
> **provar que o algoritmo funciona e que as três versões devolvem
> exatamente a mesma resposta** (coluna "idêntico" = true em todas as
> linhas), mas não é uma medição confiável de speedup — com 1 núcleo não
> existe paralelismo real disponível, só concorrência (troca de contexto).
> **Upgrade de 1 minuto:** rode os dois comandos abaixo no seu computador
> (ele tem mais de um núcleo) e me mande a tabela impressa — eu troco os
> números por medições reais de multi-core na hora:
> ```
> javac BenchmarkStandalone.java
> java BenchmarkStandalone
> ```

Escolhemos a ordenação das requisições hospitalares por prioridade porque,
na escala nacional do enunciado, é o histórico inteiro que precisa ser
reorganizado para os painéis e para o atendimento — e o custo está todo em
CPU (comparar e ordenar), não em espera de banco ou rede.

A versão sequencial usa `Collections.sort`, complexidade O(n log n): para
100 mil requisições, 371,63 ms; para 1 milhão, 1.152,92 ms — a curva sobe
de forma compatível com n log n. Como o vetor pode ser dividido em fatias
independentes — cada fatia ordenada sozinha, sem dependência entre threads
durante o processamento —, a operação é particionável. A versão com
threads divide os dados em k fatias, ordena cada fatia em paralelo
(O((n/k) log(n/k)) por thread) e junta tudo no final com um merge k-vias
(O(n log k)), a única etapa sequencial.

**Neste ambiente (1 núcleo), o speedup ficou abaixo de 1x na maioria das
combinações** — com 1 milhão de registros, o tempo subiu de 1.152,92 ms
(sequencial) para 2.229,31 ms com 8 threads (speedup 0,52x, ou seja, ficou
quase 2x mais lento). Isso é exatamente o que a Lei de Amdahl prevê no
caso limite: quando não há núcleos físicos disponíveis para executar
trabalho em paralelo de verdade, cada thread adicional só soma overhead
de criação, troca de contexto e sincronização, sem nenhum ganho real —
threads passam a atrapalhar, não ajudar. Em uma máquina com múltiplos
núcleos, o comportamento esperado se inverte: cada thread roda em um
núcleo físico distinto, e o tempo de parede cai perto de n/k até o número
de threads ultrapassar o número de núcleos.

Voltando à Mesa do DJ (Unidade 2): lá, várias threads atendiam eventos
simultâneos que chegavam de fora (I/O). Aqui é o oposto: os dados já estão
todos disponíveis em memória, e o "paralelismo" vem de dividir um único
trabalho de CPU em pedaços — é concorrência a serviço de paralelismo, não
de atender múltiplas entradas ao mesmo tempo. E, como este ambiente só tem
1 núcleo, o experimento acabou isolando exatamente esse ponto: concorrência
sem paralelismo físico disponível não acelera nada, só adiciona overhead.

A partir de um certo ponto, mais threads deixam de ajudar mesmo em máquinas
com vários núcleos: quando o número de threads ultrapassa o número de
núcleos físicos, ou quando o overhead de coordenação e o merge final
passam a pesar mais do que o tempo economizado dividindo o trabalho. Nesse
ponto, a arquitetura evolui trocando "mais threads num processo" por
particionamento horizontal de verdade — sharding dos dados entre múltiplas
instâncias/nós, processando em paralelo de fato, e agregando os resultados
parciais (o mesmo princípio de merge, só que entre máquinas em vez de
threads dentro de um processo).

**Virtual Threads (Java 21, opcional):** os tempos ficaram na mesma faixa
de ruído das threads de plataforma, como esperado — virtual threads
reduzem o custo de criar/gerenciar milhares de threads em cargas
I/O-bound (muitas threads esperando rede/disco), mas aqui a carga é 100%
CPU-bound e limitada a 1 núcleo, então não há espaço para nenhuma das duas
abordagens ganharem vantagem real uma sobre a outra.
