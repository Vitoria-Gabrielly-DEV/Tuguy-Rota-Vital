# Análise — Ordenação de requisições por prioridade (SO, Unidade 1)

> Preencha os colchetes [ ] com os números que você mediu rodando o
> benchmark (`docs/como-rodar-benchmark-ordenacao.md`). O texto abaixo já
> está pronto — só falta plugar os seus tempos reais.

Escolhemos a ordenação das requisições hospitalares por prioridade porque,
na escala nacional do enunciado, é o histórico inteiro que precisa ser
reorganizado para os painéis e para o atendimento — e o custo está todo em
CPU (comparar e ordenar), não em espera de banco ou rede.

A versão sequencial usa `Collections.sort`, com complexidade O(n log n): em
[quantidade] requisições, o tempo medido foi de [tempoSeq] ms. Como o vetor
pode ser dividido em fatias independentes — cada fatia ordenada sozinha, sem
nenhuma dependência entre threads durante o processamento — a operação é
particionável. A versão com threads divide os dados em k fatias, ordena cada
fatia em paralelo (O((n/k) log(n/k)) por thread) e junta tudo no final com um
merge k-vias (O(n log k)), que é a única etapa sequencial.

Com [threads] threads, o tempo caiu para [tempoPar] ms, um speedup de
[speedup]x — não [threads]x linear, porque o merge final e a criação/gestão
das threads não paralelizam (é a Lei de Amdahl: o ganho é limitado pela
fração do trabalho que continua sequencial). A Big-O "do algoritmo" não
muda (continua O(n log n) no total de trabalho); o que muda é o tempo de
parede, porque o trabalho é distribuído entre processadores.

Voltando à Mesa do DJ (Unidade 2): lá, várias threads atendiam eventos
simultâneos que chegavam de fora (I/O). Aqui é o oposto: os dados já estão
todos disponíveis em memória, e o paralelismo vem de dividir um único
trabalho de CPU em pedaços — é concorrência a serviço de paralelismo real,
não de atender múltiplas entradas ao mesmo tempo.

A partir de um certo ponto, mais threads deixam de ajudar: quando o
overhead de criar/coordenar threads e o custo do merge final passam a pesar
mais do que o tempo que se economiza dividindo o trabalho, ou quando o
número de threads passa do número de núcleos disponíveis na máquina. Nesse
ponto, a arquitetura evolui trocando "mais threads" por particionamento
horizontal de verdade (sharding dos dados entre múltiplas instâncias/nós,
processando em paralelo de fato, e agregando os resultados parciais — o
mesmo princípio de merge, só que entre máquinas em vez de threads).

**Opcional (Virtual Threads, Java 21):** com virtual threads, o tempo medido
foi [tempoVirtual] ms — [igual/próximo/pior] ao do pool de threads de
plataforma, como esperado: virtual threads reduzem o custo de criar/gerenciar
threads em cargas I/O-bound (muitas threads esperando), mas aqui a carga é
100% CPU-bound, então o ganho de virtual threads sobre threads de plataforma
tende a ser pequeno.
