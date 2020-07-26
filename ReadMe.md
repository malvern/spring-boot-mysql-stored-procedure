# Spring Boot MySQL Stored Procedure
This Article uses MySQL version 8.0.18 running in a docker container.

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

Stored Procedures are stored under `Stored Procedures`(tab) within the database.
     
[procedure image](/images/stored_procedure.png)

This article will focus mainly on how to create Stored Procedure using Spring Boot.

### 2.0 Basic Stored Procedure Structure
A basic Stored Procedure consists of a **procedure name** and **routine body** defined as follows.

                      CREATE PROCEDURE stored_procedure_name
                        routine_body:
                             Valid SQL routine statements

routine body can contain compound statements thus multiple SQL statements.
#### 2.1 Read Stored Procedure
Using the above stored procedure structure lets us create a Stored Procedure to retreive all customers from the database.
    
                      CREATE PROCEDURE findAllCustomers()
                       BEGIN
                         SELECT * FROM customer;
                       END
                       
Notice we have added BEGIN/END to specify a block of our SQL statement(s).

Lets define the above stored procedure within Spring application.properties file(*application.yml*).

                      customer:
                        procedure:
                               find:
                                   all: CREATE PROCEDURE findAllCustomers()
                                        BEGIN
                                           SELECT * FROM customer;
                                        END

Utilizing Spring Environment to read from stored procedure from `application.yml` file and JdbcTemplate to connect to the database.

                        public void createCustomerRetreiveProcedure() {
                             final String query = environment.getProperty("customer.procedure.find.all");
                             jdbcTemplate.execute(query);
                        }

The above code will create a Stored Procedure in MySQL database,however if a Stored procedure with the similar name exists it will throw `SQLSyntaxErrorException` highlighting that the stored procedure exists.

#### 2.2 Filtering Data (IN parameter).

Now since we know the basic stucture of stored procedure.Let consider a scenario were we want to dispay results based on a certain conditions e.g age(customers with age equal to 70years).

A SQL query can be defined as `SELECT * FROM kyc_database.customer WHERE customer.age=age;` but how do we do this in a Stored Procedure.

Stored Procedure can include parameter declarations however,when declaring these parameters we need to indicate the direction in which information flows(`output` or `input` or `both`).

In our case since we need to input age that will be used to filter records within our SQL query.

Hence we declare the input type and direction within our stored procedure as shown below.

                       CREATE PROCEDURE findCustomersByAge(IN age INTEGER)
                       BEGIN
                           SELECT * FROM kyc_database.customer WHERE customer.age=age;
                       END

 ***IN(input parameter) - The parameter value is passed in from the caller to the procedure.The procedure can assign a different value to the parameter, but this change is not visible to the caller.***
                              
 #### 2.3 Returning Data (OUTPUT parameter)
 Considering a scenario were we want to return `sum` of all the promotional points from the database.
 
 Since the result is an `output` we can explicitly indicate this within our stored Procedure as well as datatype as shown below
                     
                      CREATE PROCEDURE sumPromotionalPoints(OUT totalPoints VARCHAR(50))
                      BEGIN
                          SELECT SUM(promotion_point) INTO totalPoints FROM customer;
                      END
                      
  ***OUT(output parameter) -  The caller passes a variable as the parameter. Any value the parameter has when it is passed is ignored by the procedure, and its initial value within the procedure is NULL. The procedure sets its value, and after the procedure terminates, the parameter value is passed back from the procedure to the caller. The caller sees that value when it accesses the variable.***

#### 2.4 Filtering and Returning Data(INOUT two way)
We can combine both IN and OUT parameters as one parameter for input as well as output.

                      CREATE PROCEDURE findCustomberBySurname(INOUT surname VARCHAR(50))
                      BEGIN
                        SELECT customer.surname INTO surname  FROM  customer WHERE customer.surname=surname;
                      END
                      
***INOUT(two way) - indicates a two-way parameter that can be used both for input and for output. The value passed by the caller is the parameter’s initial value within the procedure. If the procedure changes the parameter value, that value is seen by the caller after the procedure terminates.***
                   
#### 2.5 Filtering and Returning Data(IN/OUT)
We can  explicity specify input and output parameters for our StoredProcedure as shown below
        
                     CREATE PROCEDURE findCustomerAgeByName(IN name VARCHAR(50),OUT age Integer)
                     BEGIN
                       SELECT customer.age into age  FROM customer WHERE customer.name = name;
                     END

### 3.0 Delete(Dropping) Stored Procedure
Creating a stored procedure with a name similar to an existing stored procedure will throw `SQLSyntaxErrorException` with a narrative specifying that the given procedure already exists.

There is no way we can edit stored procedure *body* apart from changing its `characteristics` only,so whenever we need to change our
stored procedure SQL statements we must first delete the existing procedure and recreate it with the new changes.

The following command is used to delete stored procedure.

                      DROP PROCEDURE [IF EXISTS] procedure_name
         
If the stored procedure does not exists it will return error.We can add `IF EXISTS` so that it returns a warning instead of error.


Calling delete procedure from Spring Boot
      
                       public void deleteStoredProcedures(String procedureName) {
                          final String queryDrop = "DROP PROCEDURE IF EXISTS " + procedureName;
                          jdbcTemplate.execute(queryDrop);
                       }
### 4.0 Calling Stored Procedure
When invorking/calling a stored procedure we use a `call statement` as shown below

                       call procedure_name([input/output parameters if required]);
                    
for simplicity of this tutorial we will only call `INOUT` procedure(`findCustomerAgeByName`) defined in section 2.5
Calling findCustomerAgeByName procedure with name = `tanaka` returns age is equal to 45.

There are many ways in which we can execute stored procedures from Spring boot.

#### 4.1  SimpleJdbcCall
When using SimpleJdbcCall we need to provide procedureName and input,output or both parametes as defined in the saved stored procedure.

SimpleJdbcCall is multi-threaded.

                      @Override
                       public Integer findCustomerByAge(String name) {
                         final SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("findCustomerAgeByName");
                         final Map<String, String> inputParameters = new HashMap<>();
                         inputParameters.put("name", name);
                         inputParameters.put("age", "@age");
                         final SqlParameterSource sqlParameterSource = new MapSqlParameterSource(inputParameters);
                         final Integer result = jdbcCall.executeObject(Integer.class, sqlParameterSource);
                          return result;
                       }
                       

Testing our code

                       @Test
                       @DisplayName("retreive customer age using name")
                        void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAge() {
                              final String name = "Tanaka";
                              final Integer age = customerRepository.findCustomerByAge(name);
                              assertThat(age).as("Customer Age").isNotNull();
                              assertThat(age).as("Age").isEqualTo(45L);
                        }
                        
#### 4.2  StoredProcedure

StoredProcedure is a superclass for object abstractions of RDBMS stored procedures.

Similar approach as in SimpleJdbcCall is then applied.

                        @Override
                        public Integer findCustomerByAgeUsingStoredProcedure(String name) {
                              final CustomStoredProcedure customStoredProcedure = new  CustomStoredProcedure(jdbcTemplate, "findCustomerAgeByName");
                              final SqlParameter sqlName = new SqlParameter("name", Types.VARCHAR);
                              final SqlOutParameter outParameter = new SqlOutParameter("age", Types.INTEGER);
                              final SqlParameter[] sqlParameters = {sqlName,outParameter};
                              customStoredProcedure.setParameters(sqlParameters);
                              customStoredProcedure.setJdbcTemplate(jdbcTemplate);
                              customStoredProcedure.compile();
                              Map<String,Object> storedProcResult = customStoredProcedure.execute(name, "age");
                              return (Integer)storedProcResult.get("age");
                        }
                        
#### 4.3  CallableStatement

CallableStatement is an interface that is used to execute SQL stored procedures.

Numbers starting from 1 are used when defining input,output or both parameters in CallableStatement.

All output parameters are registered prior the execution of the stored procedure.

Output values are retrieved after query execution using get methods.

                        @Override
                        public Integer findCustomerByAgeUsingCallableStatement(String name) throws SQLException {
                              final Connection connection = jdbcTemplate.getDataSource().getConnection();
                              final CallableStatement callableStatement = connection.prepareCall("{call findCustomerAgeByName(?, ?)}");
                              callableStatement.setString(1, name);
                              callableStatement.registerOutParameter(2, Types.INTEGER);
                              callableStatement.executeUpdate();
                              return callableStatement.getInt(2);
                        }
                        
#### 4.4  JdbcTemplate

JdbcTemplate simplifies the use of JDBC and helps to avoid common errors.(central class in Jdbc package)

                        @Override
                        public Integer findCustomerByAgeUsingJdbcStatement(String name) throws SQLException {
                               final SqlParameter nameParameter = new SqlParameter(Types.VARCHAR);
                               final SqlOutParameter outParameter = new SqlOutParameter("age", Types.INTEGER);
                               final String callStoredProcedure = "{call findCustomerAgeByName(?, ?)}";
                               Map<String, Object> resultMap = jdbcTemplate.call(connection -> {
                                    CallableStatement callableStatement = connection.prepareCall(callStoredProcedure);
                                    callableStatement.setString(1, name);
                                    callableStatement.registerOutParameter(2, Types.INTEGER);
                                    return callableStatement;
                               }, Arrays.asList(nameParameter,outParameter));
                              return (Integer)resultMap.get("age");
                        }

         

#### 5. Alter Stored Procedure

On altering Stored Procedure we can only change  the characteristics of a stored procedure but we cannot change sql body or parameters.

These parameters are optional and they can appear in any order.

   - SQL security {DEFINER|INVOKER} - runs either with the privileges of the user who created it or the user who invoked it.
    (Definer) - causes the routine to have the privileges of the user who created it. This is the default value.
    (Invoker) - causes the routine to run with the privileges of its invoking user. This means the routine has access to database objects only if that user can                     already access them otherwise.
    
  -  SQL SECURITY DEFINER enables you to create routines that access information on behalf of users who otherwise would not be able to do so.
  
  -  DETERMINISTIC or NOT DETERMINISTIC - Indicates whether the routine always produces the same result when invoked with a given set of input parameter                values(used for query optimisation).Default behavior is not Deterministic.
  
  - LANGUAGE SQL - Indicates the language in which the routine is written.
  
  - COMMENT - Specifies a descriptive string for the routine.The string is displayed by the statements that return information about routine definitions
       
                       ALTER PROCEDURE procedure_name [characteristics]
 
 In this articcle we will focus on adding a comment to the existing query and changing SQL security from Definer to Invoker.
                       
                       
                       ALTER PROCEDURE findAllCustomers
                       SQL SECURITY INVOKER
                       COMMENT 'This procedure returns all customers';
                         
 Altering stored procedure using Spring boot
 
                       public void alterCustomerRetreiveProcedure(){
                          final String query =  environment.getProperty("customer.procedure.alter.find.all");
                          jdbcTemplate.execute(query);
                       }
                       
Using all the above steps stored procedure we can present stored procedure as follows:


                       CREATE PROCEDURE procedureName ([parameters]) 
                         [characteristics]
                        routine_body:
                            Valid SQL routine statements
                                
                        
#### 6. Reading Stored Procedure Metadata.

MySQL creates INFORMATION_SCHEMA database with ROUTINES table that contains information about stored procedure.

Lets retreive information on `findAllCustomers` procedure

                       @Test
                       @DisplayName("retreive procedure information")
                        void viewProcedureInformationShouldReturnProcedureInformation() {
                                    final ProcedureDto procedureDto = customerRepository.viewStoredProceduresInformation();
                                    assertThat(procedureDto).as("procedure information object").isNotNull();
                                    assertThat(procedureDto.getRoutineSchema()).as("database where procedure is defined")
                                                                               .isEqualTo("kyc_database");
                                    assertThat(procedureDto.getName()).as("procedure name").isEqualTo("findAllCustomers");
                                    assertThat(procedureDto.getRoutineBodyDefination()).as("routine body definition(query)")
                                                                                     .isEqualTo("BEGIN Select * from customer; END");
                                    assertThat(procedureDto.getRoutineBodyLanguage()).as("routine body language").isEqualTo("SQL");
                                    assertThat(procedureDto.getDateCreated()).as("date when procedure was created")
                                                                             .isEqualTo("2020-07-25 18:37:40");
                                    assertThat(procedureDto.getComment()).as("procedure comment")
                                                                         .isEqualTo("This procedure returns all customers");
                                    assertThat(procedureDto.getDefiner()).as("procedure definer").isEqualTo("root@%");
                                    assertThat(procedureDto.getDeterminstic()).as("determinstic").isEqualTo("NO");
                                    assertThat(procedureDto.getLastAltered()).as("date when procedure was altered")
                                                                             .isEqualTo("2020-07-25 18:37:39");
                                    assertThat(procedureDto.getSecurityType()).as("security type").isEqualTo(null);
                                    assertThat(procedureDto.getSqlMode()).as("sql mode").isEqualTo("INVOKER");
                        }

#### 6. Conclusion

In this article,we illustrated how to create stored procedures utilising Spring boot properties file.How to call/execute
stored procedures using SimpleJdbc,StoredProcedure,CallableStatement and JdbcTemplate.

Code for this article is available at [SourceCode](https://github.com/malvern/spring-boot-mysql-stored-procedure)

