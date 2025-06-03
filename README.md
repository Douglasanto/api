# 📡 API - Comunicação de PCDs com a Prefeitura de Camaçari

Este projeto é uma **API RESTful** desenvolvida durante um **projeto de Iniciação Científica**, com o objetivo de facilitar a **comunicação entre Pessoas com Deficiência (PCDs)** e a **Prefeitura de Camaçari**. Por meio desta API, é possível cadastrar informações essenciais sobre cidadãos PCDs, como endereço, deficiência, benefícios e profissão, promovendo inclusão e eficiência na gestão pública.

---

## 🎯 Objetivo do Projeto

- Facilitar o cadastro de PCDs no município  
- Integrar dados relevantes para políticas públicas  
- Fornecer uma base de dados acessível aos setores responsáveis da Prefeitura  
- Aumentar a visibilidade e acompanhamento das necessidades dos cidadãos  

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot**  
- **Maven**  
- **MySQL**  
- **Postman** (para testes)  
- **Spring Data JPA**  

---

## 📁 Estrutura do Projeto

```
api/
├── controller/        # Camada de controle (REST Controllers)
├── model/             # Entidades JPA
├── service/           # Lógica de negócio
├── repository/        # Interfaces com o banco de dados
├── resources/
│   └── application.properties
└── pom.xml            # Gerenciador de dependências (Maven)
```

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+  
- Maven  
- Banco de dados configurado  

### Passos

```bash
# Clone o projeto
git clone https://github.com/GabrielMalheiros/api.git
cd api

# Configure o banco em application.properties
# Execute a aplicação
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 📮 Exemplos de Requisições (Postman)

### 🔹 1. Cadastrar um novo usuário PCD

**Endpoint:** `POST /usuarios`  
**Body (JSON):**

```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "71999999999",
  "endereco": {
    "tipoEndereco": "Residencial",
    "logradouro": "Rua das Flores",
    "bairro": "Centro",
    "cidade": "Camaçari",
    "estado": "BA",
    "cep": "42800-000"
  },
  "deficiencia": {
    "descricao": "Deficiência Auditiva"
  },
  "profissao": {
    "descricao": "Professor"
  },
  "beneficio": {
    "descricao": "BPC"
  }
}
```

---

### 🔹 2. Listar todos os usuários cadastrados

**Endpoint:** `GET /usuarios`  
**Resposta (exemplo):**

```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "71999999999",
    "endereco": {
      "tipoEndereco": "Residencial",
      "logradouro": "Rua das Flores",
      "bairro": "Centro",
      "cidade": "Camaçari",
      "estado": "BA",
      "cep": "42800-000"
    },
    "deficiencia": {
      "descricao": "Deficiência Auditiva"
    },
    "profissao": {
      "descricao": "Professor"
    },
    "beneficio": {
      "descricao": "BPC"
    }
  }
]
```

---

## 🗃️ Banco de Dados

Este projeto utiliza um banco relacional, podendo ser configurado com **MySQL** ou **PostgreSQL**.

O modelo de dados é composto por tabelas como:

- `usuarios`  
- `enderecos`  
- `deficiencias`  
- `profissoes`  
- `beneficios`  

![Imagem do WhatsApp de 2025-06-03 à(s) 09 10 41_3985325f](https://github.com/user-attachments/assets/03616d89-7bd9-4139-b23f-be2463801548)

### 🔧 Exemplo de application.properties (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pcd_db
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 👨‍💻 Autor

Desenvolvido por [Gabriel Malheiros]
