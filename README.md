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
|   GET      | /api/cidades?nome={nome}          |                                                         |
|   GET      | /api/cidades?estado={estado}      |                                                         |

### Cliente

| Método     | URI                               | Exemplo JSON                                                                   |
|------------|-----------------------------------|--------------------------------------------------------------------------------|
|   POST     | /api/clientes/                    | {"nome": "Anderson", "sexo": "M", "data_nascimento": "19/01/1989", "cidade": 1}|
|   GET      | /api/clientes/{id}                |                                                                                |
|   GET      | /api/clientes?nome={nome}         |                                                                                |
|   DELETE   | /api/clientes/{id}                |                                                                                |
|   PATCH    | /api/clientes/{id}                | {"nome": "Anderson"}                                                                               |
