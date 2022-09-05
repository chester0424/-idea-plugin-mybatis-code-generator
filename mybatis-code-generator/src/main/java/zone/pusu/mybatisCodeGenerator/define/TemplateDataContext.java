package zone.pusu.mybatisCodeGenerator.define;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateDataContext extends HashMap {
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

        put("targetFileDir", targetFileDir);
        put("targetFileName", targetFileName);

        put("setter", new TemplateDataContextCallBack(this));
    }

    public String getTargetFileDir() {
        return get("targetFileDir").toString();
    }

    public String getTargetFileName() {
        return get("targetFileName").toString();
    }
}
