# SecureMessenger
---
## About project
### Purpose of creation
This project was created in order to simplify secure communication between users.
### How it works?
#### App
The application performs the function of sending and encrypting messages, 
as well as generating encryption keys using **AES 256** keys.
#### Content encryption
The application encrypts the text of the message, 
as well as the files attached to it, using the generated encryption key, ***all other information is not encrypted***.
#### Server
The server performs the task of distributing the message and the files attached to them.
---
## Installation
### Server
Download and install ***java 11*** runtime or jdk, in case of jdk set bin folder of installed jdk to path and
create ***JAVA_HOME*** path variable with path to jdk folder.

Download ***PostgreSQL 14*** install it and create database with name **securemessenger**. Create user postgres with password 12345.

Run server jarball in cmd by command ***java -jar SecureMessengerServer.jar***
### App
Just download release apk of **SecureMessenger** and launch it.

