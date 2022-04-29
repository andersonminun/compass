# Compass
Desafio Técnico da Compass

## Funções

- Cadastro de cidade
- Cadastro de cliente
- Filtrar cidade por nome e estado
- Filtrar cliente por id
- Filtrar cliente por nome
- Remover cliente
- Alterar nome do cliente

## Rotas da API

### Cidade

| Método     | URI                               | Exemplo JSON                                            |
|------------|-----------------------------------|---------------------------------------------------------|
|   POST     | /api/cidades/                     | {"nome": "Recife", "estado": "Pernambuco"}              |
|   GET      | /api/cidades?nome=Recife          |                                                         |
|   GET      | /api/cidades?estado=Pernambuco    |                                                         |
