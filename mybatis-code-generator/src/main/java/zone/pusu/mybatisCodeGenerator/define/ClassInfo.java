package zone.pusu.mybatisCodeGenerator.define;

import java.util.Collection;

/**
 * 类型信息
 */
public class ClassInfo {
    /**
     * 类名
     */
    private String name;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径（全路径）
     */
    private String filePath;
    /**
     * 字段信息
     */
    private Collection<FieldInfo> fieldInfos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Collection<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(Collection<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
}
