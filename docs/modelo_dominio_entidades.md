# Rota Vital — Modelo de Domínio (Entidades Principais)

> Documento de apoio à entrega da Unidade 1 de POO — critério "Modelo de domínio" (40% do bloco PI).
> Base: `docs/historias-usuario.md` (HU01–HU08) + `Planejamento_Projeto_Integrador_3.pdf`.

## 1. Objetivo

Levantar as entidades de negócio do sistema Rota Vital a partir das histórias de usuário já escritas, com atributos, relacionamentos e regras de validade, servindo de base para o diagrama de classes e o CRUD da Unidade 1.

## 2. Metodologia aplicada

1. Releitura de HU01–HU08 com identificação de substantivos de negócio (candidatos a entidade) e verbos (candidatos a relacionamento).
2. Teste de "é entidade de verdade?": possui identidade própria, persiste no tempo, aparece em mais de uma história.
3. Cruzamento com o escopo oficial da Unidade 1 (Planejamento do PI): Doação, Bolsa/Hemocomponente, Estoque, Hospital, Requisição, Usuário/Funcionário.
4. Extração de atributos diretamente dos cenários Dado/Quando/Então de cada HU.
5. Mapeamento de relacionamentos e cardinalidades entre entidades.
6. Registro das regras de validade associadas a cada atributo relevante.

## 3. Entidades

### 3.1 Usuário/Funcionário
**Origem:** HU08 (cadastro/login), ator citado em todas as demais HUs.

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | chave primária |
| nome | String | obrigatório |
| emailCorporativo | String | obrigatório, único — HU08 exige "e-mail de trabalho autorizado" |
| senhaHash | String | nunca armazenar em texto puro |
| perfil | Enum | ex.: FUNCIONARIO_HEMOCENTRO, GESTOR, RESPONSAVEL_DISTRIBUICAO, RESPONSAVEL_MONITORAMENTO |

**Regra de validade:** cadastro/login não pode ser concluído com campo obrigatório vazio (HU08, Cenário 3).

### 3.2 Hospital
**Origem:** citado em HU02, HU05 como origem/destino das requisições e rotas.

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | chave primária |
| nome | String | obrigatório |
| endereco | String | usado no cálculo de rota (HU05) |
| janelaEntregaPadrao | String/Intervalo | referência para "janela de tempo" citada em HU05 |

### 3.3 Requisição
**Origem:** HU02, HU03, HU05, HU07.

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | atribuído automaticamente no cadastro (HU02, Cenário 1) |
| hospitalId | FK → Hospital | obrigatório |
| tipoABORh | Enum | usado na compatibilidade (HU04) |
| componente | Enum | hemácias, plasma, plaquetas etc. |
| quantidade | Integer | obrigatório |
| status | Enum | PENDENTE, ALOCADA, ATENDIDA — HU02 Cenário 3 exige listar "status" |
| janelaEntrega | Intervalo | usada no cálculo de rota (HU05) |
| dataHora | Timestamp | registro do momento da solicitação |

**Regra de validade:** requisição sem dado obrigatório não é registrada; sistema deve indicar o campo faltante (HU02, Cenário 2).

### 3.4 Bolsa/Hemocomponente
**Origem:** HU01, HU03, HU04.

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | chave primária |
| tipoABORh | Enum | base da checagem de compatibilidade (HU04) |
| componente | Enum | tipo, componente citados em HU01 |
| dataColeta | Date | origem: doação |
| dataValidade | Date | usada na priorização FEFO (HU03) |
| status | Enum | DISPONIVEL, ALOCADA, DESCARTADA |

**Regras de validade:**
- Só pode ser alocada se `status = DISPONIVEL` e `dataValidade` não vencida.
- Entre bolsas compatíveis, prioriza-se sempre a de menor `dataValidade` (FEFO — HU03, Cenário 2).
- Compatibilidade ABO/Rh é checada contra `tipoABORh` da requisição (HU04) — didática, não substitui protocolo clínico.

### 3.5 Estoque
**Origem:** HU01, HU07. Pode ser modelado como entidade agregadora (contagem por tipo+componente) ou como visão derivada de Bolsa — decisão de design da equipe.

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | chave primária |
| tipoABORh | Enum | |
| componente | Enum | |
| quantidadeTotal | Integer | soma de bolsas disponíveis daquele tipo/componente |

**Regra de validade:** consulta de componente sem unidades disponíveis deve informar explicitamente a indisponibilidade (HU01, Cenário 3).

### 3.6 Doação
**Origem:** não explícita nas HU01–HU08 atuais; exigida pelo escopo oficial da Unidade 1 (Planejamento do PI) e citada no protótipo Figma ("Cadastro de Doação").
⚠️ **Ação recomendada:** confirmar com o time se falta uma HU cobrindo esse fluxo, ou se ele é implícito ("toda bolsa é originada de uma doação").

| Atributo | Tipo | Observação |
|---|---|---|
| id | UUID/Long | chave primária |
| dataDoacao | Date | |
| tipoABORhColetado | Enum | |
| componenteGerado | Enum | pode gerar mais de uma Bolsa por doação |

## 4. Relacionamentos

| Origem | Relação | Destino | Cardinalidade |
|---|---|---|---|
| Hospital | solicita | Requisição | 1 : N |
| Usuário/Funcionário | registra | Requisição | 1 : N |
| Requisição | aloca | Bolsa/Hemocomponente | N : N |
| Doação | origina | Bolsa/Hemocomponente | 1 : N |
| Bolsa/Hemocomponente | compõe | Estoque | N : 1 |

## 5. Rastreabilidade (entidade → história de usuário)

| Entidade | HUs relacionadas |
|---|---|
| Usuário/Funcionário | HU08 (todas as demais como ator) |
| Hospital | HU02, HU05 |
| Requisição | HU02, HU03, HU05, HU07 |
| Bolsa/Hemocomponente | HU01, HU03, HU04 |
| Estoque | HU01, HU07 |
| Doação | nenhuma HU explícita — gap a validar |

## 6. Próximos passos

- [ ] Validar com o squad se "Doação" precisa de HU própria antes do checkpoint da Entrega U1 (semana 8–9).
- [ ] Transformar este modelo em diagrama de classes (UML) para anexar ao artefato de "Modelo documentado" exigido pela rubrica.
- [ ] Vincular este documento ao ticket correspondente no board PI3-E3 e solicitar revisão do time.
- [ ] Confirmar decisão de design: Estoque como entidade própria vs. view agregada sobre Bolsa.
