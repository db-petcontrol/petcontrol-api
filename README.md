# 🐾 PetControl API

⛔ *Seção em construção*

<br>

🔗 API disponível em produção: [link](https://petcontrol-api-h7vz.onrender.com/actuator/health)

*⚠️ A aplicação pode levar alguns segundos para responder na primeira requisição devido ao cold start da plataforma.*

<br>

## 🖥️ Frontend da aplicação

Este projeto possui uma interface web desenvolvida separadamente para consumo da API:

🔗 Frontend: [link](https://petcontrol-app.onrender.com)

🔗 Repositório: [link](https://github.com/db-petcontrol/petcontrol-app)


<br>

## ✨ Principais funcionalidade
⛔ *Seção em construção*

<br>

## 🛠️ Tecnologias utilizadas

### Backend
- ☕ Java 21 — linguagem principal da aplicação
- 🍃 Spring Boot — framework para API REST
- 💾 PostgreSQL — banco relacional principal
- 💾 H2 Database — banco em memória para testes e desenvolvimento
- 🔁 MapStruct — mapeamento entre DTOs e entidades
- 📦 Lombok — redução de boilerplate no código

### Infraestrutura & Build
- 🐳 Docker — containerização da aplicação e padronização do ambiente
- 🪽 Flyway — versionamento e controle de migrations do banco de dados
- ⚙️ GitHub Actions — automação de CI/CD (build, testes e validações)
- 🪝 Git Hooks — validações locais antes de commits (ex: lint e padrões de código)

### Qualidade de Código
- 🧪 JUnit — framework principal para testes unitários
- 🧪 Mockito — criação de mocks para testes isolados
- 📊 JaCoCo — análise e relatório de cobertura de testes
- 🧹 Checkstyle — validação de padrões de código seguindo boas práticas de estilo Java
- 🎨 Spotless — formatação automática do código baseada no Google Java Format

### Documentação
- 📄 Swagger / OpenAPI — documentação interativa e explorável da API REST

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
