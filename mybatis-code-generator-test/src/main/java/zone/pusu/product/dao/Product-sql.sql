


----------------------------
---- Product
---- Chester 2024-01-10 19:51:33
----------------------------

---- mysql
CREATE TABLE Product (
    ID nvarchar(50) PRIMARY KEY,
    NAME nvarchar(50),
    CODE nvarchar(50),
    WEIGHT decimal(50),
    PLACE_OF_PRODUCTION nvarchar(50),
    PRICE decimal(50)
);

---- mssql
CREATE TABLE Product (
    ID nvarchar(50) PRIMARY KEY,
    NAME nvarchar(50),
    CODE nvarchar(50),
    WEIGHT decimal(50),
    PLACE_OF_PRODUCTION nvarchar(50),
    PRICE decimal(50)
);

---- oracle
CREATE TABLE Product (
    ID NVARCHAR2(50) PRIMARY KEY,
    NAME NVARCHAR2(50),
    CODE NVARCHAR2(50),
    WEIGHT NUMBER(50),
    PLACE_OF_PRODUCTION NVARCHAR2(50),
    PRICE NUMBER(50)
);


