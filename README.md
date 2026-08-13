# Solução Tuguy - Rota Vital — Projeto Integrador (3º Semestre ADS)

> **Última atualização:** `13/08/2026` — Semana `1` do roadmap
> Atualize este cabeçalho a cada checkpoint semanal.

Gestão e distribuição de hemocomponentes na rede de sangue, inspirado no fluxo da Hemorrede/SUS (coleta/doação → hemocentro → estoque → hospitais). Aplicação web em **Java/Spring Boot**, com dados **exclusivamente sintéticos** (sem dados reais de doadores/pacientes — LGPD).

---

## 1. Ficha do projeto

| Campo | Descrição |
|---|---|
| **Problema central** | Garantir o componente certo (compatível e válido), no lugar certo, no tempo certo e na temperatura certa — evitando desabastecimento, descarte por vencimento e risco ao paciente. |
| **Produto esperado** | App web que gerencia estoque, recebe requisições hospitalares, aloca bolsas por compatibilidade + FEFO, calcula rotas respeitando cadeia fria/janelas de tempo, e exibe painéis de estoque/demanda e monitoramento. |
| **Complexidade** | Média (escopo controlado: poucos componentes, grafo limitado, dados sintéticos). |
| **Limites de escopo** | Só dados sintéticos; telemetria simulada; compatibilidade ABO/Rh didática; grafo de rotas limitado; sem integração com sistemas oficiais. |

---

## 2. Organização da squad

| Integrante |  Área/Papel no PI | Contato |
|---|---|---|
| `Vitória Gabrielly Silva` | `Líder, Desenvolvedora` | `@Vitoria-Gabrielly-DEV` |
| `Nome` | | |
| `Nome` | | |
| `Nome` | | |
| `Nome` | | |

- **Repositório:** `link`
- **Ambiente/deploy:** `link`
- **Quadro de acompanhamento:** `link` (colunas: Squad | Disciplina | Entrega | Unidade | Semana prevista | Status | Dependências | Riscos | Observações)

---

## 3. Status por disciplina (atualizar semanalmente)

| Disciplina | Entrega em foco | Status | Bloqueios | Próximo passo |
|---|---|---|---|---|
| POO | | 🔴/🟡/🟢 | | |
| AED | | 🔴/🟡/🟢 | | |
| EST | | 🔴/🟡/🟢 | | |
| SO | | 🔴/🟡/🟢 | | |
| RSD | | 🔴/🟡/🟢 | | |

🟢 no prazo · 🟡 atenção · 🔴 bloqueado/atrasado

---

## 4. Roadmap (Projeto 3)

| Semana | Foco | Disciplinas | Status |
|---|---|---|---|
| 1 | Kickoff do PI e organização das squads | Todas | ☐ |
| 2–3 | Modelagem do domínio e escopo de dados/algoritmos | POO, AED, RSD | ☐ |
| 4–5 | Setup de infraestrutura e início dos algoritmos | SO, RSD, AED | ☐ |
| 6–7 | Primeira sprint — convergência das entregas U1 | Todas | ☐ |
| **8–9** | **Fechamento e checkpoint da Entrega U1** | Todas | ☐ |
| 10–11 | Replanejamento U2 + compatibilidade/integração | POO, AED | ☐ |
| 12–13 | Telemetria, concorrência e painéis | SO, RSD, EST | ☐ |
| 14–15 | Consolidação integrada + preparação da apresentação | Todas | ☐ |
| **16–17** | **Apresentação final e avaliação de processo/colaboração** | Todas | ☐ |


---

## 5. Entregas por unidade

### Unidade 1 (fecha semana 8–9)
- [ ] **POO** — Spring Boot com domínio + CRUD das entidades, regras de validade, deploy inicial
- [ ] **AED** — Grafo + Dijkstra, hash de estoque, fila de prioridade (FEFO)
- [ ] **EST** — Indicadores + análise descritiva + painel inicial
- [ ] **SO** — Pipeline CI/CD + primeiro deploy + threads em processo real
- [ ] **RSD** — Diagrama de topologia + requisitos de rede + protocolos/APIs definidos

### Unidade 2 (fecha semana 15–16)
- [ ] **POO** — App integrada (alocação, requisições, rotas, painéis) + testes + CI/CD
- [ ] **AED** — Compatibilidade ABO/Rh integrada + análise de complexidade
- [ ] **EST** — Análise probabilística (desabastecimento, descarte) + painel consolidado
- [ ] **SO** — Arquitetura em 3 cenários + orçamento + sincronização
- [ ] **RSD** — Benchmarking (latência/vazão/erros) + painel de rede + plano de migração

> Rubricas completas de cada critério estão no documento de planejamento oficial (`Planejamento_Projeto_Integrador_3.pdf`).

---

## 6. Riscos ativos

| Risco | Impacto | Mitigação em andamento |
|---|---|---|
| App virar CRUD sem integração dos algoritmos | Alto | |
| Grafo de rotas grande demais | Médio | |
| Compatibilidade ABO/Rh simplista | Médio | |
| Deploy/CI-CD deixado para o fim | Alto | |
| Telemetria/painéis desconectados do app | Médio | |


---

## 7. Checkpoints de comunicação docente

- Reunião quinzenal Projeto ↔ disciplinas técnicas: semanas 3, 5, 7, 9, 11, 13, 15
- Checkpoint de integração U1: semana 8–9 | U2: semana 15–16

---
