# PROJETO LiterAlura📚 

(Português brasileiro - Brazilian portuguese)

⚠️LEIA-ME!⚠️  

> Este projeto realiza a busca, listagem e armazenamento de livros e autores utilizando dados reais obtidos via **API Gutendex**. A aplicação é interativa via terminal, e persiste as informações no banco de dados **PostgreSQL**, permitindo consultas organizadas por título, autor, idioma e ano.

---
## ❗Sobre o projeto

Projeto desenvolvido durante o curso da **Alura**, no programa **Oracle Next Education - ONE (8º Grupo)**. Projeto desenvolvido durante o curso da **Alura**, no programa **Oracle Next Education - ONE (8º Grupo)**. O objetivo da aplicação é **consumir a API Gutendex** para buscar informações sobre livros e autores, salvando-os no banco de dados local com **Spring Boot** e **JPA**, permitindo consultas posteriores através de um menu interativo.

---
## ⚙️ Roteiro ("Script")

> A seguir, estão listadas as funcionalidades e a lógica principal no arquivo `Principal.java` e classes associadas:

- [X] Menu interativo com opções para:
  - Buscar livros por título diretamente na API Gutendex.
  - Listar todos os livros salvos no banco.
  - Listar todos os autores cadastrados.
  - Listar autores vivos em determinado ano.
  - Listar livros filtrados por idioma.
- [X] Requisição HTTP à API Gutendex (em `ConsumoAPI.java`);
- [X] Uso de biblioteca JSON (Jackson/Gson) para processar os dados da API;
- [X] Salvamento dos dados via **Spring Data JPA** no PostgreSQL;
- [X] Impressão formatada dos resultados no console;
- [X] Estrutura modular com **Service**, **Repository** e **Entity**.

---
## 🚀 Layout do Aplicativo

> Por ser um projeto de terminal, não possui interface gráfica (GUI), mas sim interação por linha de comando (CLI). O menu é exibido diretamente no console com opções numeradas para fácil navegação. A interação é feita por **linha de comando (CLI)**, com menu numerado para facilitar a navegação e execução das opções. Se desejar uma experiência mais previsível de entrada ("stdin") durante o desenvolvimento, prefira rodar pelo terminal/console com `mvn spring-boot:run` em vez de usar o botão "Run" da IDE, pois o "Run window" nem sempre entrega "stdin" de forma confiável. Constando uma imagem:

![[image_2025-08-08_14-03-55.png]]

---
## ▶️ Como rodar a aplicação

1. Clone o repositório:
   ```bash
   git clone https://github.com/luizfelipe010496/challenge-literAlura_github_luizfelipe010496_pt_br-main.git
   ```
2. Softwares necessários e links)
- **Java JDK** (instale ao menos uma das versões abaixo):
  - Java SE Development Kit 17.0.12 (JDK 17): https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
  - Java SE Development Kit 21.0.7 (JDK 21): https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html
  - Java SE Development Kit 24.0.2 (versão atual): https://www.oracle.com/br/java/technologies/downloads/
  - Java SE (padrão): https://www.java.com/pt-BR/download/
- **IDE** (recomendada): IntelliJ IDEA (Community ou Ultimate): https://www.jetbrains.com/pt-br/idea/download/
- **PostgreSQL** (banco de dados):
  - Página oficial: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
  - Exemplo versão (15.4): https://www.manageengine.com/products/desktop-central/software-installation/silent_install_PostgreSQL-15-(15.4).html
- **Maven** (geralmente já vem integrado na IDE; use a wrapper ou instale localmente)

3. Abra no **IntelliJ IDEA** (ou outra IDE Java com suporte a Maven).

4. Configure as variáveis de ambiente para acesso ao PostgreSQL:
   - **Windows (CMD/PowerShell)**:
```
setx DB_URL "jdbc:postgresql://localhost:5432/literalura"
setx DB_USER "postgres"
setx DB_PASSWORD "sua_senha"
```
- **Linux/macOS**:
```
export DB_URL="jdbc:postgresql://localhost:5432/literalura"
export DB_USER="postgres"
export DB_PASSWORD="sua_senha"
```
5. **Definição manual (Windows GUI)**:
	-.Abra o Menu Iniciar → Pesquisar “Variáveis de Ambiente” → Editar variáveis do sistema → Variáveis de Ambiente.
	 -Em `Variáveis do usuário` ou `Variáveis do sistema`, clique em `Novo...` e adicione `DB_URL`, `DB_USER`, `DB_PASSWORD` com os valores apropriados.
	-Abra um novo terminal para que as variáveis fiquem disponíveis.

6. Certifique-se de ter o PostgreSQL rodando e um banco chamado e criado localmente: **literalura_db**.
7. Execute:
```
mvn spring-boot:run
```
---
## 🔧 Linguagens, Libs e Projetos

- 💻 **Java 17** ou superior
- 🖥 **Spring Boot 3+**
- 📦 **Spring Data JPA**
- 🗄 **PostgreSQL**
-  - 📜 **Jackson/Gson** para parse de JSON
- 🔌 **HttpClient** (Java nativo) para chamadas à API
- 🌐 **Gutendex API** ([https://gutendex.com/](https://gutendex.com/)) - API externa de consumida! Consta um imagem:
 
 ![](https://cdn1.gnarususercontent.com.br/1/103811/ee7f204e-447d-4c64-9feb-25d634dc9e44.png)

---
## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:

- Java 17 ou superior instalado e configurado no PATH
- IDE Java (IntelliJ IDEA recomendado)
- PostgreSQL instalado e rodando
- Conexão com a Internet para acessar a API Gutendex
- Maven configurado no projeto

---
## 🛠️ Dicas e solução de problemas comuns

### 1) Erro: `Cannot load driver class: org.postgresql.Driver`
- Confirme se a dependência `org.postgresql:postgresql` está no `pom.xml` (scope `runtime`).
- Recarregue o Maven (Maven > Reload) e rode `mvn clean package`.

### 2) Redirect / Status 301 na chamada HTTP
Se a sua requisição à API retornar `301` (moved permanently), configure o `HttpClient` para seguir redirects:
```java
HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
```
E sempre use `https://` nas URLs externas quando disponível.

### 3) Imports `javax.*` vs `jakarta.*`
Se usar **Spring Boot 3.x**, atualize imports `javax.persistence.*` → `jakarta.persistence.*` e `javax.transaction.Transactional` → `jakarta.transaction.Transactional`.

### 4) Problemas com stdin ao rodar pela IDE
Algumas IDEs não encaminham stdin corretamente. Use o terminal (console) para rodar a aplicação interativa:
```bash
mvn spring-boot:run
```

### 5) Jackson e serialização de datas
Se usar `LocalDate`/`LocalDateTime`, adicione no `pom.xml`:
```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```
O Spring Boot normalmente registra o módulo automaticamente, mas você pode registrá-lo manualmente no `ObjectMapper` se necessário.

### 6) Ajuste `application.properties` (usar variáveis de ambiente)
Coloque no `src/main/resources/application.properties`:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
> Em desenvolvimento `spring.jpa.hibernate.ddl-auto=update` é prático. Em produção, prefira gerenciar migrations (Flyway/Liquibase).

### 7) Build e execução
**Via Maven (terminal):**
```bash
mvn clean package
mvn spring-boot:run
```
Ou executar jar gerado:
```bash
java -jar target/literalura-app-0.0.1-SNAPSHOT.jar
```

**Via IntelliJ:** Configure a Application Run Configuration com a `Main class = br.com.alura.challenge.luiz.literalura_app.LiteraluraAppApplication` e execute. Se o menu for interativo, prefira rodar pelo terminal com `mvn spring-boot:run` para garantir stdin disponível.

---
## 😄 Seja um dos contribuidores!

> Quer fazer parte desse projeto? Clique [AQUI](CONTRIBUTING.md) para saber como contribuir!

---
## 🤝 Desenvolvedores/Colaboradores  

| [<img src="https://avatars.githubusercontent.com/u/30264786?v=4" width=115><br><sub>Luiz Ferreira</sub>](https://github.com/luizfelipe010496) |  [<img src="https://avatars.githubusercontent.com/u/4975968?s=200&v=4" width=115><br><sub>Alura Team</sub>](https://github.com/alura-cursos) |  [<img src="https://www.oracle.com/a/ocom/img/rh03-one-br-logo.png" width=115><br><sub>Oracle Next Education</sub>](https://github.com/oracle) |
| :---: | :---: | :---: |

---
## 📝 Licença

(License)  

<p>Distribuído sob a licença pública livre (No License).</p>

2025 - LiterAlura📖☕
