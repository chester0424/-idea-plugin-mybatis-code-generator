package zone.pusu.mybatisCodeGenerator.setting;

/**
 * 主要配置信息
 */
public class SettingMain {
    private String author = "";

    /**
     * 对象配置文件路径
     */
    private String configFileSavePath = "./dao/";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getConfigFileSavePath() {
        return configFileSavePath;
    }

    public void setConfigFileSavePath(String configFileSavePath) {
        this.configFileSavePath = configFileSavePath;
    }
}
