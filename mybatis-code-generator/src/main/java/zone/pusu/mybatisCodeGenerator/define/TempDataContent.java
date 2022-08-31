package zone.pusu.mybatisCodeGenerator.define;

public class TempDataContent {
    GenerateMybatisConfigClass config;

    private String fileDir;
    private String fileName;

    public TempDataContent(GenerateMybatisConfigClass config, String fileDir, String fileName) {
        this.config = config;
        this.fileDir = fileDir;
        this.fileName = fileName;
    }

    public GenerateMybatisConfigClass getConfig() {
        return config;
    }

    public void setConfig(GenerateMybatisConfigClass config) {
        this.config = config;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
