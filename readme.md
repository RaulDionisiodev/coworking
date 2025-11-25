
---
## Perfis de Ambiente e Conexão com o Banco de Dados

A aplicação utiliza perfis do Spring Boot para facilitar a configuração de diferentes ambientes (desenvolvimento, teste e produção).

### Perfis disponíveis

- **dev**: Usa banco de dados H2 em memória para desenvolvimento local.
- **test**: Usa banco de dados H2 em memória para testes automatizados.
- **default (produção)**: Usa banco de dados MySQL.

### Como funciona a seleção de perfil

O perfil ativo é definido pela propriedade `spring.profiles.active` no arquivo `application.properties`.  
Exemplo:
```
spring.profiles.active=dev
```

### Configurações de banco de dados

- As configurações padrão ficam em `application.properties`.
- Configurações específicas de cada perfil ficam em:
    - `application-dev.properties` (H2 para desenvolvimento)
    - `application-test.properties` (H2 para testes)

#### Exemplo de URLs de conexão

- **H2 (dev/test):**
  ```
  spring.datasource.url=jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  ```
  Acesse o console H2 em: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  Use a URL: `jdbc:h2:mem:devdb`  
  Usuário: `sa`  
  Senha: (em branco)

- **MySQL (produção):**
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/coworkingdb
  spring.datasource.username=SEU_USUARIO
  spring.datasource.password=SUA_SENHA
  ```

### Como trocar de perfil

Para rodar com outro perfil, defina a variável de ambiente ou altere o valor em `application.properties`:
```
spring.profiles.active=dev
```
Ou execute com argumento:
```
-Dspring.profiles.active=dev
```

---

Adapte conforme necessário para o seu projeto.