<#function getColumnType jdbcType db len>
    <#switch jdbcType>
        <#case 'INTEGER'>
            <#switch db>
                <#case 'mysql'>
                    <#return 'int'>
                <#case 'mssql'>
                    <#return 'int'>
                <#case 'oracle'>
                    <#return 'INTEGER'>
                <#default >
                    <#return 'int'>
            </#switch>
        <#case 'DECIMAL'>
            <#switch db>
            <#--                <#assign lenArr = len?split(",")>-->
                <#case 'mysql'>
                    <#return 'decimal('+len+')'>
                <#case 'mssql'>
                    <#return 'decimal('+len+')'>
                <#case 'oracle'>
                    <#return 'NUMBER('+len+')'>
                <#default >
                    <#return '-'>
            </#switch>
        <#case 'VARCHAR'>
            <#switch db>
                <#case 'mysql'>
                    <#if len?number lt 2000>
                        <#return 'nvarchar('+len+')'>
                    <#else>
                        <#return 'ntext'>
                    </#if>
                <#case 'mssql'>
                    <#if len?number lt 2000>
                        <#return 'nvarchar('+len+')'>
                    <#else>
                        <#return 'nvarchar(max)'>
                    </#if>
                <#case 'oracle'>
                    <#if len?number lt 2000>
                        <#return 'NVARCHAR2('+len+')'>
                    <#else>
                        <#return 'CLOB'>
                    </#if>
                <#default >
                    <#return '-'>
            </#switch>
        <#case 'BLOB'>
            <#switch db>
                <#case 'mysql'>
                    <#return 'BLOB'>
                <#case 'mssql'>
                    <#return 'VARBINARY'>
                <#case 'oracle'>
                    <#return 'BLOB'>
                <#default >
                    <#return '-'>
            </#switch>
        <#case 'DATETIME'>
            <#switch db>
                <#case 'mysql'>
                    <#return 'DATETIME'>
                <#case 'mssql'>
                    <#return 'date'>
                <#case 'oracle'>
                    <#return 'DATE'>
                <#default >
                    <#return '-'>
            </#switch>
        <#default >
            <#return '-'>
    </#switch>
</#function>



----------------------------
---- ${tableName}
---- <#if authorName?? && authorName != "">${authorName} </#if>${.now?string("yyyy-MM-dd HH:mm:ss")}
----------------------------

---- mysql
CREATE TABLE ${tableName} (
<#if keyField??>
    ${keyField.columnName?upper_case} ${getColumnType(keyField.jdbcType?upper_case,'mysql',keyField.extend.DBLength)} PRIMARY KEY,
</#if>
<#list nonKeyFields as field>
    ${field.columnName?upper_case} ${getColumnType(field.jdbcType?upper_case,'mysql',field.extend.DBLength)}<#sep>,
</#list>

);

---- mssql
CREATE TABLE ${tableName} (
<#if keyField??>
    ${keyField.columnName?upper_case} ${getColumnType(keyField.jdbcType?upper_case,'mssql',keyField.extend.DBLength)} PRIMARY KEY,
</#if>
<#list nonKeyFields as field>
    ${field.columnName?upper_case} ${getColumnType(field.jdbcType?upper_case,'mssql',field.extend.DBLength)}<#sep>,
</#list>

);

---- oracle
CREATE TABLE ${tableName} (
<#if keyField??>
    ${keyField.columnName?upper_case} ${getColumnType(keyField.jdbcType?upper_case,'oracle',keyField.extend.DBLength)} PRIMARY KEY,
</#if>
<#list nonKeyFields as field>
    ${field.columnName?upper_case} ${getColumnType(field.jdbcType?upper_case,'oracle',field.extend.DBLength)}<#sep>,
</#list>

);

<#-- 修改模板生成路径 -->
${callback.setTargetFileDir(targetFileDir+"\\dao")}
${callback.setTargetFileName(className+"-sql.sql")}