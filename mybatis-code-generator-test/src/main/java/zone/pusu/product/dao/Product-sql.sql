----------------------------
---- Product
---- chester 2024-01-02 16:24:09
----------------------------
CREATE TABLE Product (
    ID NVARCHAR2(50) PRIMARY KEY,
    NAME NVARCHAR2(50),
    CODE NVARCHAR2(50),
    WEIGHT NUMBER(18,2),
    PLACE_OF_PRODUCTION NVARCHAR2(50),
    PRICE NUMBER(18,2)
);


