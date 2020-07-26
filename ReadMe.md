# Spring Boot MySQL Stored Procedure
The following Article uses MySQL 8.0.18 running in a docker container.

**mysql-docker-compose file**
   
                   version: "3"
                   services:
                     mysql-stored-procedure-database-container:
                       image: mysql:8.0.18
                       container_name: mysql-stored-procedure-database-container
                       environment:
                         MYSQL_ROOT_PASSWORD: developer@@
                       volumes:
                         - /data/mysql
                       ports:
                         - 127.0.0.1:3307:3306/tcp

 Database username and password are  `root` and  `developer@@` respectively.
 
 ### 1.0 Introduction
Stored Procedure encapsulates a group of SQL statements(SELECT,UPDATE,DELETE) into a single unit and promotes code reuse.

Stored Procedures plays a crucial role in fine turning of query performance,security and other database operations.

They are stored under stored procedures(tab) within the database.
     


          
 
 There are many ways in which we can create a Stored procedure but for the simplicity of this article
 we will focus mainly on creation of Mysql stored Procedure using spring boot.
 
 Lets start by looking at the general structure of a Stored Procedure.
 
 - When you drop a database, any stored routines in the database are also dropped.
