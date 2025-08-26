.# Totempoo

## Descrição

Bem-vindo ao **Totempoo**! Este é um projeto desenvolvido em Java com o framework Spring Boot, que simula um sistema de totem para pedidos em um restaurante. A ideia é proporcionar uma experiência interativa e prática para os usuários, permitindo que façam seus pedidos de forma simples e rápida, tudo isso com uma interface amigável em HTML e CSS.

## Tecnologias Utilizadas

- **Java**: OpenJDK 17 LTS
- **Spring Boot**: Framework para desenvolvimento de aplicações Java
- **Maven**: Gerenciador de dependências e automação de projetos
- **HTML/CSS**: Para a interface do usuário
- **H2 Database**: Banco de dados em memória para persistência de dados

## Estrutura do Projeto

O Totempoo segue o padrão MVC (Model-View-Controller), que organiza a aplicação em três partes interconectadas:

- **Model**: Classes que representam os dados e a lógica de negócios (ex: `Pedido`, `Produto`, `Cliente`).
- **View**: Arquivos HTML que exibem a interface do usuário, utilizando Thymeleaf para renderização dinâmica.
- **Controller**: Classes que gerenciam as requisições e interagem com os serviços (ex: `TotemController`, `PedidoController`).
- **Service**: Contém a lógica principal da aplicação e gerencia a persistência de dados (ex: `PedidoArquivoService`).

## Funcionalidades

O Totempoo oferece diversas funcionalidades para facilitar a experiência do usuário:

- Cadastro de clientes, onde os usuários podem inserir seus dados.
- Seleção de produtos, incluindo hambúrgueres, bebidas e acompanhamentos.
- Um resumo do pedido antes da confirmação, permitindo que os usuários revisem suas escolhas.
- Processamento de pagamentos, com opções como dinheiro, Pix e cartão.
- Persistência de pedidos em um arquivo de texto, garantindo que os dados sejam salvos.

## Como Executar o Projeto

### Pré-requisitos

Antes de começar, você precisará ter algumas ferramentas instaladas:

- Java OpenJDK 17 LTS
- Maven
- VSCode (opcional, mas recomendado para facilitar o desenvolvimento)

### Passos para Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu_usuario/totempoo.git
   cd totempoo
2.Compile o projeto usando Maven:
```bash
 mvn clean install
```
3.Execute a aplicação:
```bash
 mvn spring-boot:run
```
4. acessar a aplicação no computador
```bash
 mvn spring-boot:run
```







   
   


