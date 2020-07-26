#drop database if exists kyc_database;
drop table  if exists customer;
create table customer(
                         id Integer not null AUTO_INCREMENT,
                         name VARCHAR(50),
                         surname VARCHAR(50),
                         city VARCHAR(50),
                         promotion_point INTEGER,
                         age INTEGER,
                     primary key (id)
);