CREATE TABLE ${tableName} (
<#list fields as field>
    ${field.columnName?upper_case} ${field.extend.oracle_db?upper_case}<#sep>,
</#list>

)

<#-- 修改模板生成路径 -->
${callback.setTargetFileDir(targetFileDir+"\\dao")}${callback.setTargetFileName(className+"-sql.sql")}