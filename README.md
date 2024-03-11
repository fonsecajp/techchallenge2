# techChallenge 2 - Grupo 9

## Descrição
Este projeto é um sistema de controle de parquímetro para gerenciamento da entrada e saída de veículos.
Atualmente, sistema aceita três tipos de veículos: motocicleta, carro e caminhonete.
#### Cenários
Um veículo só pode entrar se não estiver atualmente no estacionamento e só pode sair se já tiver entrado e não houver uma saída registrada para sua entrada.
Além disso, é possível visualizar todos os registros do parquímetro para um veículo específico.
As exceções são tratadas para evitar casos inválidos.

## Instalação e Uso
### Pré-requisitos:
- Maven
- Docker (para a segunda opção de instalação)

### Opção 1: Uso com banco de dados H2 em memória
1. Realize o build da aplicação com Maven.
2. Execute a classe principal da aplicação. Isto usará o profile `dev` que configura um banco de dados em memória H2.

### Opção 2: Uso com Docker e banco de dados PostgreSQL
1. Certifique-se de que o Maven build foi realizado com sucesso.
2. Navegue até o diretório raiz do projeto.
3. Execute `docker-compose up` para subir o banco de dados PostgreSQL e a aplicação.

## Documentação da API
A documentação da API está disponível via Swagger. Após iniciar a aplicação, a documentação pode ser acessada em:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints
Os endpoints para entrada, saída e busca de veículos estão definidos no diretório `src/main/resources`.

## Notas
- Este projeto não possui atualmente uma seção para contribuições externas.
- Não há informações sobre licença; o projeto está atualmente sem uma licença definida.