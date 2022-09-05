package zone.pusu.mybatisCodeGenerator.define;

import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateDataContext { //extends HashMap {
    private String className;
    private String packageName;
    private String fileName;
    private String fileDir;
    private String filePath;
    private String tableName;

    private List<Field> fields;
    private Field keyField;
    private List<Field> nonKeyFields;
    private String targetFileDir;
    private String targetFileName;

    private Tool tool = new Tool();

    public static TemplateDataContext build(ClassInfo classInfo, GenerateConfig config, String targetFileDir, String targetFileName) {
        TemplateDataContext templateDataContext = new TemplateDataContext();
        templateDataContext.className = classInfo.getName();
        templateDataContext.packageName = classInfo.getPackageName();
        templateDataContext.fileName = classInfo.getFileName();
        templateDataContext.fileDir = new File(classInfo.getFilePath()).getParent();
        templateDataContext.filePath = classInfo.getFilePath();
        templateDataContext.tableName = config.getTableName();
        List<Field> fields = new ArrayList<>();
        for (GenerateConfigField configField : config.getFields()) {
            if (!configField.isIgnore()) {
                Field field = new Field();
                field.name = configField.getName();
                field.javaType = configField.getJavaType();
                field.columnName = configField.getColumnName();
                field.jdbcType = configField.getJdbcType();
                field.primaryKey = configField.isPrimaryKey();
                field.typeHandler = configField.getTypeHandler();

                Map<String, Object> contextFieldExtend = new LinkedHashMap();
                configField.getExtend().forEach((key, value) -> {
                    contextFieldExtend.put(key, value);
                });
                field.extend = contextFieldExtend;
                fields.add(field);
            }
        }
        templateDataContext.fields = fields;

        //key field
        Optional<Field> firstOrNull = fields.stream().filter(i -> i.primaryKey == true).findFirst();
        if (firstOrNull.isPresent()) {
            templateDataContext.keyField = firstOrNull.get();
            templateDataContext.nonKeyFields = fields.stream().filter(i -> i != firstOrNull.get()).collect(Collectors.toList());

        }

        templateDataContext.targetFileDir = targetFileDir;
        templateDataContext.targetFileName = targetFileName;

        return templateDataContext;
    }


    public String getClassName() {
        return className;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    private void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDir() {
        return fileDir;
    }

    private void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTableName() {
        return tableName;
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Field> getFields() {
        return fields;
    }

    private void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getKeyField() {
        return keyField;
    }

    private void setKeyField(Field keyField) {
        this.keyField = keyField;
    }

    public List<Field> getNonKeyFields() {
        return nonKeyFields;
    }

    private void setNonKeyFields(List<Field> nonKeyFields) {
        this.nonKeyFields = nonKeyFields;
    }

    public String getTargetFileDir() {
        return targetFileDir;
    }

    private void setTargetFileDir(String targetFileDir) {
        this.targetFileDir = targetFileDir;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    private void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public Tool getTool() {
        return tool;
    }

    private void setTool(Tool tool) {
        this.tool = tool;
    }
}

class Field {
    String name;
    String javaType;
    String columnName;
    String jdbcType;
    boolean primaryKey;
    String typeHandler;
    Map<String, Object> extend;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getJavaType() {
        return javaType;
    }

    private void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnName() {
        return columnName;
    }

    private void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    private void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    private void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    private void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    private void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}

class Tool {
    public String toLowerCaseFirstOne(String source) {
        return StringUtil.toLowerCaseFirstOne(source);
    }
}