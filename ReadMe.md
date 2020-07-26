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

They are stored under `Stored Procedures`(tab) within the database.
     
[procedure image](/images/stored_procedure.png)

This article will focus mainly on how to create Stored Procedure using Spring Boot.

### 2.0 Basic Stored Procedure Structure
A basic Stored Procedure consists of a **procedure name** and **routine body** defined in the format below.

           CREATE PROCEDURE stored_procedure_name
           routine_body:
                 Valid SQL routine statements

routine body can contain compound statements.
#### 2.1 Read Stored Procedure
Using the above stored procedure structure lets create a procedure to retreive all customers from the database.
    
         CREATE PROCEDURE findAllCustomers()
         BEGIN
            SELECT * FROM customer;
         END
Notice we have added BEGIN/END to specify a block of our compound statement(s).
Lets define the above stored procedure within Spring application.properties file(*yml file was used for this article for easy readbility*).

               customer:
                   procedure:
                         find:
                           all: CREATE PROCEDURE findAllCustomers()
                                BEGIN
                                   SELECT * FROM customer;
                                END

Utilizing Spring Environment to read from stored procedure from `application.yml` file and JdbcTemplate 
to connect to the database.

                           public void createCustomerRetreiveProcedure() {
                              final String query = environment.getProperty("customer.procedure.find.all");
                             jdbcTemplate.execute(query);
                           }

The above code will create a Stored Procedure in MySQL database,however if a Stored procedure with the similar name exists it will throw `SQLSyntaxErrorException` highlighting that the stored procedure exists.
Moreover,in case of bad SQL statement it will throw `SQLException`.

#### 2.2 Filtering Records Stored Procedure.
Now since we know the basic stucture of stored procedure.Let consider a scenario were we need to dispay results based on a certain conditions e.g age.
A SQL query can be defined as `SELECT * FROM kyc_database.customer WHERE customer.age=age;` but how do we do this in a Stored Procedure.

Stored Procedure can include parameter declarations however,when declaring these parameters we need to indicate the direction in which information flows(`output` or `input` or `both`).

In our case since we need to input age that is going to be used within our SQL filter.

Hence we declare the input type and direction within our stored procedure as shown below.

                              CREATE PROCEDURE findCustomersByAge(IN age INTEGER)
                              BEGIN
                                 SELECT * FROM kyc_database.customer WHERE customer.age=age;
                              END
 
                              
 #### 2.3 OUT
 Considering a scenario were we  need to return `sum` of all the promotional points from the given dataset.
 
 Since the result is an `output` we can explicitly indicate this within our stored Procedure as well as datatype as shown below
                     
                     CREATE PROCEDURE sumPromotionalPoints(OUT totalPoints VARCHAR(50))
                     BEGIN
                          SELECT SUM(promotion_point) INTO totalPoints FROM customer;
                     END

#### 2.4 IN OUT combined
Scenario were we can combine both IN and OUT

                    CREATE PROCEDURE findCustomberBySurname(INOUT surname VARCHAR(50))
                    BEGIN
                        SELECT customer.surname INTO surname  FROM  customer WHERE customer.surname=surname;
                    END
                    
                    
#### 2.5 both INOUT procedure
        
                  CREATE PROCEDURE findCustomerAgeByName(IN name VARCHAR(50),OUT age Integer)
                  BEGIN
                    SELECT customer.age into age  FROM customer WHERE customer.name = name;
                  END


