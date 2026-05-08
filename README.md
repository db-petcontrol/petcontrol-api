# 🐾 PetControl API

O PetControl é uma API REST desenvolvida em Spring Boot para cadastro e gerenciamento de pets disponíveis para adoção. O sistema permite criar, listar, consultar, editar e remover pets, fornecendo uma arquitetura organizada, documentação interativa e integração com aplicações frontend.

<br>

🔗 Disponível em produção (Swagger/OpenAPI): [https://petcontrol-api-h7vz.onrender.com/swagger-ui/index.html](https://petcontrol-api-h7vz.onrender.com/swagger-ui/index.html)

*⚠️ A aplicação pode levar alguns segundos para responder na primeira requisição devido ao cold start da plataforma.*

<br>

## 🖥️ Frontend da aplicação

Este projeto possui uma interface web desenvolvida separadamente para consumo da API:

🔗 Frontend (Produção): [https://petcontrol-app.onrender.com](https://petcontrol-app.onrender.com)

🔗 Repositório: [https://github.com/db-petcontrol/petcontrol-app](https://github.com/db-petcontrol/petcontrol-app)


<br>

## ✨ Principais funcionalidade
- Cadastro de pets com validação de dados
- Listagem paginada de pets ordenados por nome
- Visualização detalhada de um pet por ID
- Edição de informações de pets cadastrados
- Exclusão de pets
- Regras de validação para dados do pet (campos obrigatórios, caracteres válidos e tamanho)
- Testes unitários e de integração.

<br>

## 🛠️ Tecnologias utilizadas

| Tecnologia        | Categoria                |
| ----------------- | ------------------------ |
| Java              | Linguagem                |
| Spring Boot       | Framework                |
| PostgreSQL        | Banco de Dados           |
| H2 Database       | Banco em Memória         |
| MapStruct         | Mapeamento               |
| Lombok            | Redução de Boilerplate   |
| Docker            | Containerização          |
| Flyway            | Migrations               |
| GitHub Actions    | CI/CD                    |
| Git Hooks         | Validações Locais        |
| JUnit             | Testes Unitários         |
| Mockito           | Mocking                  |
| JaCoCo            | Cobertura de Testes      |
| Checkstyle        | Padronização de Código   |
| Spotless          | Formatação               |
| Swagger / OpenAPI | Documentação da API REST |


<br>

## 🚀 Como rodar o projeto

### Pré-requisitos
Antes de começar, você precisará ter instalado em sua máquina:

- **Docker**
- **Git**
- **Java** (versão 21 - LTS recomendado)
- **Maven** (ou utilizar o wrapper `mvnw` incluído no projeto)

### 1. Clone o repositório
   ```bash
   # Clone o repositório
   git clone https://github.com/db-petcontrol/petcontrol-api.git

   # Acesse a pasta do projeto
   cd petcontrol-api
   ```

### 2.1. Opção 1: Rodar tudo com Docker
#### 2.1.1 Suba backend + banco de dados
   ```bash
   docker-compose up -d --build
   ```

### 2.2. Opção 2: Rodar backend local + banco no Docker
#### 2.2.1 Suba apenas o banco de dados:
   ```bash
   docker-compose up -d postgres
   ```

#### 2.2.2. Rode a aplicação localmente
   ```bash
   ./mvnw spring-boot:run
   ```

<br>

## 🧪 Testes
O projeto possui:
- Testes unitários
- Testes de integração (cenários felizes)

Para executar os testes automatizados:
   ```bash
   ./mvnw test
   ```

<br>

## 📊 Cobertura de código
O projeto utiliza JaCoCo para análise de cobertura.

Para gerar o relatório:
   ```bash
   ./mvnw clean verify
   ```
*OBS: O relatório também é gerado após a execução dos testes.*

O relatório estará disponível em:
   ```bash
   target/site/jacoco/index.html
   ```
Abra o arquivo index.html no navegador para visualizar os detalhes da cobertura.

<br>

## 📜 Padronização de código

O projeto utiliza **Checkstyle** e **Spotless** para garantir consistência, legibilidade e padronização do código.

Para verificar padrões de código (Checkstyle)

   ```bash
   ./mvnw checkstyle:check
   ```

Para corrigir formatação automaticamente (Spotless)

   ```bash
   ./mvnw spotless:apply
   ```

## 📄 Documentação da API
A documentação da API está disponível via Swagger:

   ```bash
   http://localhost:8080/swagger-ui/index.html
   ```
Após iniciar a aplicação, acesse o link acima para:

- Visualizar os endpoints disponíveis
- Testar requisições diretamente
- Consultar contratos da API

<br>

<hr>

<p align="center">
    Feito com apoio de ☕ por 👩‍💻
    <a href="https://www.linkedin.com/in/gabrieladecastrolaurindo" target="_blank">
    Gabriela de Castro Laurindo
    </a>
    e
    <a href="https://br.linkedin.com/in/rachel-pizane" target="_blank">
    Rachel Pizane Maia
    </a>
</p>
