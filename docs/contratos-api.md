# Rota Vital — Contratos da API

## Objetivo

Este documento define os contratos HTTP da API do Rota Vital para as operações atualmente priorizadas pelo projeto.

Os contratos especificam:

* método HTTP;
* endpoint;
* parâmetros de entrada;
* formato das requisições JSON;
* formato das respostas JSON;
* códigos HTTP de sucesso;
* códigos HTTP de erro;
* regras de negócio relacionadas à operação.

Os contratos devem ser utilizados como referência para a implementação do back-end e para os testes da aplicação.

---

# 1. Consultar estoque de hemocomponentes

**História relacionada:** HU01 — Consultar estoque de hemocomponentes

**Operação-alvo do teste de concorrência de SO:** sim.

## Endpoint

```http
GET /api/estoque
```

A operação permite consultar a disponibilidade de hemocomponentes por tipo sanguíneo e componente.

## Parâmetros de consulta

Os filtros são opcionais.

| Parâmetro    | Tipo   | Obrigatório | Descrição                       |
| ------------ | ------ | ----------- | ------------------------------- |
| `tipoABORh`  | string | Não         | Tipo sanguíneo a ser consultado |
| `componente` | string | Não         | Hemocomponente a ser consultado |

### Exemplo

```http
GET /api/estoque?tipoABORh=O%2B&componente=HEMACIAS
```

## Resposta de sucesso

**HTTP 200 OK**

```json
{
  "itens": [
    {
      "tipoABORh": "O+",
      "componente": "HEMACIAS",
      "quantidadeTotal": 25,
      "disponivel": true
    }
  ]
}
```

## Consulta sem unidades disponíveis

Quando não existirem unidades disponíveis para o tipo sanguíneo ou componente consultado, a API deve retornar uma resposta de sucesso indicando explicitamente a indisponibilidade.

**HTTP 200 OK**

```json
{
  "itens": [
    {
      "tipoABORh": "O+",
      "componente": "PLAQUETAS",
      "quantidadeTotal": 0,
      "disponivel": false
    }
  ]
}
```

Essa resposta segue a **RN08**, que determina que a indisponibilidade não deve ser tratada como erro genérico.

## Erros

### HTTP 400 Bad Request

Utilizado quando os parâmetros fornecidos forem inválidos.

```json
{
  "codigo": "PARAMETRO_INVALIDO",
  "mensagem": "Um ou mais parâmetros informados são inválidos."
}
```

---

# 2. Registrar requisição hospitalar

**História relacionada:** HU02 — Registrar requisição hospitalar

## Endpoint

```http
POST /api/requisicoes
```

## Requisição

**Content-Type:** `application/json`

Os seguintes campos são obrigatórios, conforme a RN01:

* `hospitalId`;
* `tipoABORh`;
* `componente`;
* `quantidade`.

### Exemplo

```json
{
  "hospitalId": 10,
  "tipoABORh": "O+",
  "componente": "HEMACIAS",
  "quantidade": 5
}
```

## Resposta de sucesso

**HTTP 201 Created**

A requisição registrada deve receber um identificador automático e o status inicial `PENDENTE`, conforme a RN02.

```json
{
  "id": 123,
  "hospitalId": 10,
  "tipoABORh": "O+",
  "componente": "HEMACIAS",
  "quantidade": 5,
  "status": "PENDENTE"
}
```

## Erros

### HTTP 400 Bad Request — campo obrigatório ausente

```json
{
  "codigo": "CAMPO_OBRIGATORIO_AUSENTE",
  "mensagem": "O campo 'quantidade' é obrigatório."
}
```

Esse comportamento está relacionado à RN01.

### HTTP 400 Bad Request — tipo sanguíneo inválido

```json
{
  "codigo": "TIPO_SANGUINEO_INVALIDO",
  "mensagem": "O tipo sanguíneo informado é inválido."
}
```

Esse comportamento está relacionado à RN03.

---

# 3. Consultar requisições hospitalares

**História relacionada:** HU02 — Registrar requisição hospitalar, Cenário 3

## Endpoint

```http
GET /api/requisicoes
```

A operação deve retornar as requisições hospitalares cadastradas e seus respectivos status.

## Resposta de sucesso

**HTTP 200 OK**

```json
{
  "requisicoes": [
    {
      "id": 123,
      "hospitalId": 10,
      "tipoABORh": "O+",
      "componente": "HEMACIAS",
      "quantidade": 5,
      "status": "PENDENTE"
    }
  ]
}
```

---

# 4. Formato padrão de erros

Quando ocorrer um erro de validação ou de regra de negócio, a API deverá retornar um objeto JSON contendo, no mínimo:

```json
{
  "codigo": "CODIGO_DO_ERRO",
  "mensagem": "Descrição do erro."
}
```

Os códigos devem permitir identificar de forma objetiva o motivo da falha.

## Códigos definidos

| Código                      | HTTP | Situação                        |
| --------------------------- | ---: | ------------------------------- |
| `PARAMETRO_INVALIDO`        |  400 | Parâmetro de consulta inválido  |
| `CAMPO_OBRIGATORIO_AUSENTE` |  400 | Campo obrigatório não informado |
| `TIPO_SANGUINEO_INVALIDO`   |  400 | Tipo ABO/Rh inválido            |

---

# 5. Relação com o teste de concorrência de SO

O endpoint utilizado como alvo do teste de concorrência será:

```http
GET /api/estoque
```

O teste deverá simular múltiplas requisições simultâneas a esse endpoint.

Fluxo previsto:

```text
Múltiplas threads/requisições
            ↓
GET /api/estoque
            ↓
EstoqueService
            ↓
Banco de dados
            ↓
Dados de estoque
```

O teste deverá observar, conforme a implementação:

* tempo de resposta;
* quantidade de requisições processadas;
* ocorrência de erros ou falhas;
* consistência dos dados retornados;
* comportamento do banco de dados diante de acessos concorrentes.

O endpoint definido neste documento deverá ser utilizado como referência para a implementação do teste de concorrência.

---

# 6. Observações

Este documento define os contratos das operações priorizadas nesta etapa do projeto.

Os demais endpoints relacionados às histórias de usuário HU03 a HU08 deverão ser especificados conforme suas respectivas implementações e regras de negócio forem desenvolvidas e validadas pelo squad.

O projeto utiliza exclusivamente dados sintéticos. As informações de compatibilidade ABO/Rh possuem finalidade didática e não substituem protocolos clínicos.