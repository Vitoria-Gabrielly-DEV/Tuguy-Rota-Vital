# Definição do alvo do teste de concorrência — SO

## Objetivo

Definir explicitamente a operação de processamento em volume de dados que será utilizada como alvo do teste de concorrência da disciplina de Sistemas Operacionais (SO).

## Operação escolhida

**Consulta de estoque de hemocomponentes.**

A operação corresponde à **HU01 — Consultar estoque de hemocomponentes**, definida no documento `docs/historias-usuario.md`.

### Objetivo da operação

Permitir que funcionários do hemocentro consultem a disponibilidade de hemocomponentes, considerando informações como tipo sanguíneo, componente e quantidade disponível.

### Alvo do teste

O teste de concorrência deverá simular **múltiplas requisições simultâneas de consulta ao estoque**, avaliando o comportamento da aplicação quando vários usuários/processos solicitam os dados ao mesmo tempo.

Fluxo previsto:

```text
Múltiplas threads/requisições
            ↓
Endpoint de consulta de estoque
            ↓
Camada de serviço
            ↓
Banco de dados
            ↓
Dados de estoque
```

## O que será avaliado

O teste deverá permitir observar, conforme a implementação do endpoint:

* comportamento da aplicação com acessos simultâneos;
* tempo de resposta sob concorrência;
* quantidade de requisições processadas;
* ocorrência de erros ou falhas;
* consistência dos dados retornados;
* comportamento do banco de dados diante de acessos concorrentes.

## Endpoint

O endpoint definitivo de consulta de estoque deverá ser definido no contrato da API e implementado pelo back-end.

**Operação-alvo:** consulta/listagem de estoque de hemocomponentes.

**Método HTTP esperado:** `GET`.

O caminho definitivo do endpoint será registrado em `docs/contratos-api.md`.

## Observação sobre a implementação atual

No momento da definição deste documento, a branch utilizada para o desenvolvimento ainda possui principalmente a estrutura inicial de cadastro de usuários. A operação de consulta de estoque pertence ao domínio funcional definido nas histórias de usuário e será utilizada como alvo do teste quando o respectivo endpoint estiver implementado.

A definição antecipada deste alvo tem como finalidade evitar que o teste de concorrência seja desenvolvido sobre uma operação diferente da acordada para o projeto.

## Relação com os requisitos do projeto

* **HU01 — Consultar estoque de hemocomponentes**
* **SO — Threads em processo real**
* **SO — Pipeline CI/CD e primeiro deploy**
* Processamento concorrente de dados de estoque