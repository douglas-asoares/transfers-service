# Transfers Service API

###### Powered by Douglas Aparecido Soares

<p align="center">
 <a href="#-about-the-project">About</a> •
 <a href="#-features">Features</a> •
 <a href="#-how-to-run-the-project">How to run</a> • 
 <a href="#-tools">Tools</a> • 
 <a href="#-documentation">Documentation</a> 
</p>

## 💻 About the project

- This API allows the management of transfer between two bank accounts

## ⚙️ Features

- [x] Create banks data
- [x] Create customer data
- [x] Create accounts data
- [x] Deposit money into a valid account
- [x] Management of transfers between two bank accounts

## 🚀 How to run the project

### Prerequisites

Before starting, you will need to have the following tools installed on your machine:

- [Git](https://git-scm.com)
- [Java 15](https://openjdk.java.net/projects/jdk/15/)
- [Maven](https://maven.apache.org/install.html)
- [MongoDB server](https://www.mongodb.com/docs/v4.4/installation/)

#### 🎲 Rodando o projeto

```bash

# Clone this repository:
$ git clone https://github.com/douglas-asoares/transfers-service.git

# Change Java version to Java 15:
$ sudo update-alternatives --config java (Linux example)

# Install the dependencies:
$ mvn clean install

# Run the application:
$ TransfersServiceApplication.java

# The server will start on port :8080

# The local MongoDB server must be running and a database named transfers-service must be created in your local database

```

## 🛠 Tools

The following tools were used in the project building:

- **Java** 15
- **Spring boot** 2.5.2.RELEASE
- **JUnit 4**
- **Maven** 3.5+
- **MongoDB**
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [Info](http://localhost:8080/actuator/info)

## 🛠 Documentação

- **[Swagger](http://localhost:8080/swagger-ui.html)**

