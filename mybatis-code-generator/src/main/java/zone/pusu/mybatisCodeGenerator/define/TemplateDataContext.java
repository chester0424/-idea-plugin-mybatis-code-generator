package zone.pusu.mybatisCodeGenerator.define;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.apache.commons.lang.ArrayUtils;
import zone.pusu.mybatisCodeGenerator.setting.SettingMainStoreService;
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

                Map<String, Object> contextFieldExtend = new LinkedHashMap<>(configField.getExtend());
                field.put("extend", contextFieldExtend);
                fields.add(field);
            }
        }
        put("fields", fields);

        Optional<Map<String, Object>> firstOrNull = fields.stream().filter(i -> Boolean.parseBoolean(i.get("primaryKey").toString())).findFirst();
        if (firstOrNull.isPresent()) {
            put("keyField", firstOrNull.get());
            put("nonKeyFields", fields.stream().filter(i -> i != firstOrNull.get()).collect(Collectors.toList()));
        }

        put("fieldTypeImports", fieldTypeImports(classInfo, config));

        put(TARGET_FILE_DIR, targetFileDir);
        put(TARGET_FILE_NAME, targetFileName);

        put("callback", new TemplateDataContextCallBack(this));

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project currentProject = projects[0];
        put("projectBasePath", currentProject.getBasePath());

        put("authorName", SettingMainStoreService.getInstance().getState().getAuthor());
    }

    public String getTargetFileDir() {
        return get(TARGET_FILE_DIR).toString();
    }

    public String getTargetFileName() {
        return get(TARGET_FILE_NAME).toString();
    }

    private String[] fieldTypeImports(ClassInfo classInfo, GenerateConfig config) {
        HashSet<String> fieldTypeImports = new HashSet<>();
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            // 过滤掉忽略的字段
            Optional<GenerateConfigField> generateConfigField = config.getFields().stream().filter(i -> i.getName().equals(fieldInfo.getName()) && !i.isIgnore()).findFirst();
            if (generateConfigField.isPresent()) {
                String[] javaTypes = javaTypeAnalysis(fieldInfo.getType());
                for (String javaType : javaTypes) {
                    if (TypeUtil.primitiveTypeAndWrappedTypes.keySet().stream().anyMatch(javaType::contains)) {
                        continue;
                    }
                    fieldTypeImports.add(javaType);
                }
            }
        }
        return fieldTypeImports.toArray(String[]::new);
    }

    private String[] javaTypeAnalysis(String javaType) {
        // is array
        if (javaType.endsWith("[]")) {
            String type = StringUtil.trimEnd(javaType, "[]");
            return javaTypeAnalysis(type);
        }
        // is generic
        if (javaType.contains("<")) {
            String type = javaType.substring(0, javaType.indexOf('<'));
            String subJavaType = javaType.substring(javaType.indexOf("<") + 1, javaType.length() - 1);
            String[] subTypes = javaTypeAnalysis(subJavaType);
            return (String[]) ArrayUtils.addAll(new String[]{type}, subTypes);
        }
        return new String[]{javaType};
    }
}
