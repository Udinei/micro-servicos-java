# 📖 Guia de Leitura - Documentação iCompras

## Bem-vindo à Documentação do Sistema iCompras!

Esta documentação foi organizada em 5 categorias principais para facilitar a consulta.

---

## 🚀 Começar Aqui

Se você está iniciando no projeto, comece por:

1. **[Especificação Geral](ESPECIFICACOES/01_Especificacao_Geral.md)** - Entenda o que é o sistema
2. **[Visão Geral da Arquitetura](ARQUITETURA/01_Visao_Geral.md)** - Veja como foi desenhado
3. **[Modelo de Dados](MODELOS/01_Modelo_Dados.md)** - Entenda o banco de dados

---

## 📋 Navegação por Perfil

### Para Product Owner / Gerente de Projeto
1. [Especificação Geral](ESPECIFICACOES/01_Especificacao_Geral.md)
2. [Regras de Negócio - Pedidos](REGRAS_NEGOCIO/03_Pedidos.md)
3. [Regras de Negócio - Pagamentos](REGRAS_NEGOCIO/04_Pagamentos.md)
4. [Fluxos de Negócio](ARQUITETURA/04_Fluxos_Negocio.md)

### Para Desenvolvedor Backend
1. [Padrões e Convenções](ARQUITETURA/02_Padroes_Convencoes.md)
2. [Entidades Principais](MODELOS/02_Entidades_Principais.md)
3. [Banco de Dados](ARQUITETURA/03_Banco_Dados.md)
4. [Especificação do Serviço](ESPECIFICACOES/02_Servico_Clientes.md) (escolher serviço)
5. [Regras de Negócio](REGRAS_NEGOCIO/01_Clientes.md) (escolher domínio)

### Para DBA / DevOps
1. [Banco de Dados](ARQUITETURA/03_Banco_Dados.md)
2. [Modelo de Dados](MODELOS/01_Modelo_Dados.md)

### Para Testador / QA
1. [Especificações](ESPECIFICACOES/) - Todos os arquivos
2. [Regras de Negócio](REGRAS_NEGOCIO/) - Todos os arquivos
3. [Validações](REGRAS_NEGOCIO/05_Validacoes.md)

---

## 🗂️ Conteúdo por Diretório

### ESPECIFICACOES/
Documentos detalhando **o quê** cada serviço faz.

- `01_Especificacao_Geral.md` - Visão geral do projeto
- `02_Servico_Clientes.md` - Endpoints e DTOs de Clientes
- `03_Servico_Produtos.md` - Endpoints e DTOs de Produtos
- `04_Servico_Pedidos.md` - Endpoints e DTOs de Pedidos
- `05_Integracao_Bancaria.md` - Integração com banco (futuro)

**Use quando:** Precisa saber quais endpoints existem e o que enviam/retornam

### REGRAS_NEGOCIO/
Documentos detalhando **como** as regras de negócio funcionam.

- `01_Clientes.md` - Regras para clientes
- `02_Produtos.md` - Regras para produtos
- `03_Pedidos.md` - Regras para pedidos
- `04_Pagamentos.md` - Regras para pagamentos
- `05_Validacoes.md` - Regras de validação gerais

**Use quando:** Precisa entender a lógica por trás de uma feature

### ARQUITETURA/
Documentos sobre **como** foi desenhada a solução.

- `01_Visao_Geral.md` - Arquitetura geral do sistema
- `02_Padroes_Convencoes.md` - Padrões de código e convenções
- `03_Banco_Dados.md` - Schema, índices e otimizações
- `04_Fluxos_Negocio.md` - Fluxos step-by-step

**Use quando:** Precisa entender a arquitetura e padrões

### MODELOS/
Documentos sobre **dados** do sistema.

- `01_Modelo_Dados.md` - Diagrama ER e definição de tabelas
- `02_Entidades_Principais.md` - Classes JPA, DTOs e Enums

**Use quando:** Precisa entender os modelos de dados

---

## 🔍 Encontrar Informações Específicas

### Por Tópico

| Tópico | Arquivo |
|--------|---------|
| Como criar um novo pedido? | [Fluxos de Negócio](ARQUITETURA/04_Fluxos_Negocio.md) |
| Quais campos um cliente precisa ter? | [Especificação de Clientes](ESPECIFICACOES/02_Servico_Clientes.md) |
| O que acontece quando um pedido é cancelado? | [Regras de Pedidos](REGRAS_NEGOCIO/03_Pedidos.md) |
| Como integrar com o banco? | [Especificação Geral](ESPECIFICACOES/01_Especificacao_Geral.md) |
| Qual é o schema do banco? | [Banco de Dados](ARQUITETURA/03_Banco_Dados.md) |
| Quais são os status válidos de um pedido? | [Regras de Pedidos](REGRAS_NEGOCIO/03_Pedidos.md) |
| Como validar um email? | [Validações](REGRAS_NEGOCIO/05_Validacoes.md) |
| Quais endpoints existem? | [ESPECIFICACOES/](ESPECIFICACOES/) |

### Por Serviço

#### Serviço de Clientes
- Especificação: [02_Servico_Clientes.md](ESPECIFICACOES/02_Servico_Clientes.md)
- Regras: [01_Clientes.md](REGRAS_NEGOCIO/01_Clientes.md)
- Entidades: [02_Entidades_Principais.md](MODELOS/02_Entidades_Principais.md) (seção Cliente)

#### Serviço de Produtos
- Especificação: [03_Servico_Produtos.md](ESPECIFICACOES/03_Servico_Produtos.md)
- Regras: [02_Produtos.md](REGRAS_NEGOCIO/02_Produtos.md)
- Entidades: [02_Entidades_Principais.md](MODELOS/02_Entidades_Principais.md) (seção Produto)

#### Serviço de Pedidos
- Especificação: [04_Servico_Pedidos.md](ESPECIFICACOES/04_Servico_Pedidos.md)
- Regras: [03_Pedidos.md](REGRAS_NEGOCIO/03_Pedidos.md)
- Fluxos: [04_Fluxos_Negocio.md](ARQUITETURA/04_Fluxos_Negocio.md)
- Entidades: [02_Entidades_Principais.md](MODELOS/02_Entidades_Principais.md) (seções Pedido e ItemPedido)

---

## 💡 Dicas de Uso

### Desenvolvedor querendo implementar uma nova feature
1. Leia a [Especificação](ESPECIFICACOES/) relevante
2. Leia as [Regras de Negócio](REGRAS_NEGOCIO/) relacionadas
3. Leia os [Padrões e Convenções](ARQUITETURA/02_Padroes_Convencoes.md)
4. Implemente seguindo o padrão

### Testador criando casos de teste
1. Leia a [Especificação](ESPECIFICACOES/) do serviço
2. Leia as [Regras de Negócio](REGRAS_NEGOCIO/) relacionadas
3. Leia as [Validações](REGRAS_NEGOCIO/05_Validacoes.md)
4. Crie testes baseados nesses cenários

### Debugando um bug
1. Vá para a [Especificação](ESPECIFICACOES/) relevante
2. Vá para as [Regras de Negócio](REGRAS_NEGOCIO/) relacionadas
3. Compare o comportamento esperado vs observado
4. Corrija

---

## 📊 Estatísticas da Documentação

- **Total de Arquivos**: 12
- **Páginas Estimadas**: ~50
- **Imagens/Diagramas**: Múltiplos
- **Última Atualização**: 22/01/2026
- **Status**: Em Desenvolvimento ✏️

---

## 🤝 Contribuindo

Ao adicionar uma nova feature:

1. Atualize a [Especificação Geral](ESPECIFICACOES/01_Especificacao_Geral.md) se necessário
2. Crie/atualize especificação do serviço em `ESPECIFICACOES/`
3. Crie/atualize regras de negócio em `REGRAS_NEGOCIO/`
4. Atualize os [Padrões](ARQUITETURA/02_Padroes_Convencoes.md) se houver novo padrão
5. Atualize os [Fluxos](ARQUITETURA/04_Fluxos_Negocio.md) se houver novo fluxo
6. Atualize os [Modelos](MODELOS/) se houver nova entidade
7. Atualize o [Banco de Dados](ARQUITETURA/03_Banco_Dados.md) se houver alteração

---

## ❓ FAQ

**P: Por que há dois arquivos sobre especificação?**  
R: `01_Especificacao_Geral.md` é o overview, e `02_Servico_*.md` são especificações detalhadas de cada serviço.

**P: Onde vejo os endpoints da API?**  
R: Em `ESPECIFICACOES/` em cada arquivo de serviço.

**P: Onde estão as classes do código?**  
R: As estruturas estão em `MODELOS/02_Entidades_Principais.md`. O código real está nos arquivos `.java` do projeto.

**P: Qual arquivo devo ler primeiro?**  
R: Depende do seu perfil. Veja a seção "Navegação por Perfil" acima.

---

**🎉 Boa leitura! Se tiver dúvidas, consulte a documentação específica de cada domínio.**

