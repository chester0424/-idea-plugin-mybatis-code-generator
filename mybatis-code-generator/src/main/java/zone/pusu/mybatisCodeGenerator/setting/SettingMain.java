package zone.pusu.mybatisCodeGenerator.setting;

/**
 * 主要配置信息
 */
public class SettingMain {
    private String authorName = "";

    /**
     * 对象配置文件路径
     */
    private String configFileSavePath = "./dao/";

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getConfigFileSavePath() {
        return configFileSavePath;
    }

    public void setConfigFileSavePath(String configFileSavePath) {
        this.configFileSavePath = configFileSavePath;
    }
}
