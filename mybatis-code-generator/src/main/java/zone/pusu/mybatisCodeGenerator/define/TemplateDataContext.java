package zone.pusu.mybatisCodeGenerator.define;

import zone.pusu.mybatisCodeGenerator.tool.StringUtil;
import zone.pusu.mybatisCodeGenerator.tool.TypeUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateDataContext extends HashMap {

    public static final String TARGET_FILE_DIR = "targetFileDir";
    public static final String TARGET_FILE_NAME = "targetFileName";

    public TemplateDataContext(ClassInfo classInfo, GenerateConfig config, String targetFileDir, String targetFileName) {
        put("className", classInfo.getName());
        put("packageName", classInfo.getPackageName());
        put("fileName", classInfo.getFileName());
        put("fileDir", new File(classInfo.getFilePath()).getParent());
        put("filePath", classInfo.getFilePath());
        put("tableName", config.getTableName());
        List<Map<String, Object>> fields = new ArrayList<>();
        for (GenerateConfigField configField : config.getFields()) {
            if (!configField.isIgnore()) {
                Map<String, Object> field = new LinkedHashMap<>();
                field.put("name", configField.getName());
                field.put("javaType", configField.getJavaType());
                field.put("javaTypeShort", StringUtil.getLastPart(configField.getJavaType(), "."));
                field.put("columnName", configField.getColumnName());
                field.put("jdbcType", configField.getJdbcType());
                field.put("primaryKey", configField.isPrimaryKey());
                field.put("typeHandler", configField.getTypeHandler());

                Map<String, Object> contextFieldExtend = new LinkedHashMap();
                configField.getExtend().forEach((key, value) -> {
                    contextFieldExtend.put(key, value);
                });
                field.put("extend", contextFieldExtend);
                fields.add(field);
            }
        }
        put("fields", fields);

        Optional<Map<String, Object>> firstOrNull = fields.stream().filter(i -> Boolean.valueOf(i.get("primaryKey").toString()) == true).findFirst();
        if (firstOrNull.isPresent()) {
            put("keyField", firstOrNull.get());
            put("nonKeyFields", fields.stream().filter(i -> i != firstOrNull.get()).collect(Collectors.toList()));
        }

        put("fieldTypeImports", fieldTypeImports(classInfo));

        put(TARGET_FILE_DIR, targetFileDir);
        put(TARGET_FILE_NAME, targetFileName);

        put("callback", new TemplateDataContextCallBack(this));
    }

    public String getTargetFileDir() {
        return get(TARGET_FILE_DIR).toString();
    }

    public String getTargetFileName() {
        return get(TARGET_FILE_NAME).toString();
    }

    private String[] fieldTypeImports(ClassInfo classInfo) {
        HashSet<String> fieldTypeImports = new HashSet<>();
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            String javaType = fieldInfo.getType();
            // 基本类型则不引入
            if (TypeUtil.primitiveTypeAndWrappedTypes.keySet().stream().filter(i -> javaType.indexOf(i) >= 0).count() > 0) {
                continue;
            }
            fieldTypeImports.add(javaType);
        }
        return fieldTypeImports.toArray(String[]::new);
    }
}
