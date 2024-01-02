----------------------------
---- ${tableName}
---- <#if authorName?? && authorName != "">${authorName} </#if>${.now?string("yyyy-MM-dd HH:mm:ss")}
----------------------------
CREATE TABLE ${tableName} (
<#if keyField??>
    ${keyField.columnName?upper_case} ${keyField.extend.DbType?upper_case} PRIMARY KEY,
</#if>
<#list nonKeyFields as field>
    ${field.columnName?upper_case} ${field.extend.DbType?upper_case}<#sep>,
</#list>

);

<#-- 修改模板生成路径 -->
${callback.setTargetFileDir(targetFileDir+"\\dao")}
${callback.setTargetFileName(className+"-sql.sql")}