package zone.pusu.mybatisCodeGenerator.define;

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

    private List<TemplateDataContextField> fields;
    private TemplateDataContextField keyField;
    private List<TemplateDataContextField> nonKeyFields;
    private String targetFileDir;
    private String targetFileName;

    private TemplateDataContextTool tool = new TemplateDataContextTool();

    public static TemplateDataContext build(ClassInfo classInfo, GenerateConfig config, String targetFileDir, String targetFileName) {
        TemplateDataContext templateDataContext = new TemplateDataContext();
        templateDataContext.className = classInfo.getName();
        templateDataContext.packageName = classInfo.getPackageName();
        templateDataContext.fileName = classInfo.getFileName();
        templateDataContext.fileDir = new File(classInfo.getFilePath()).getParent();
        templateDataContext.filePath = classInfo.getFilePath();
        templateDataContext.tableName = config.getTableName();
        List<TemplateDataContextField> fields = new ArrayList<>();
        for (GenerateConfigField configField : config.getFields()) {
            if (!configField.isIgnore()) {
                TemplateDataContextField field = new TemplateDataContextField();
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
        Optional<TemplateDataContextField> firstOrNull = fields.stream().filter(i -> i.primaryKey == true).findFirst();
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

    public List<TemplateDataContextField> getFields() {
        return fields;
    }

    private void setFields(List<TemplateDataContextField> fields) {
        this.fields = fields;
    }

    public TemplateDataContextField getKeyField() {
        return keyField;
    }

    private void setKeyField(TemplateDataContextField keyField) {
        this.keyField = keyField;
    }

    public List<TemplateDataContextField> getNonKeyFields() {
        return nonKeyFields;
    }

    private void setNonKeyFields(List<TemplateDataContextField> nonKeyFields) {
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

    public TemplateDataContextTool getTool() {
        return tool;
    }

    private void setTool(TemplateDataContextTool tool) {
        this.tool = tool;
    }
}

