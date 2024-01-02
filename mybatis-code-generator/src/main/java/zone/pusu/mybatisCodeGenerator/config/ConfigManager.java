package zone.pusu.mybatisCodeGenerator.config;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import zone.pusu.mybatisCodeGenerator.tool.FileUtil;
import zone.pusu.mybatisCodeGenerator.tool.ObjectUtil;
import zone.pusu.mybatisCodeGenerator.tool.ProjectUtil;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ConfigManager
 */
public class ConfigManager {

    private static final String relConfigPath = ".idea/mfcg";

    public static Config getLocalConfig() throws JAXBException {
        Project project = ProjectUtil.getProject();
        if (project != null) {
            String basePath = project.getBasePath();
            String localConfigRoot = Paths.get(basePath, relConfigPath).toString();
            // read config file
            String configFileName = Paths.get(localConfigRoot, "config.xml").toString();
            File configFile = new File(configFileName);
            if (configFile.exists()) {
                String configFileContent = FileUtil.readFile(configFile.getPath());
                Config config = ObjectUtil.xmlStringToObject(Config.class, configFileContent);
                if (config != null) {
                    // save template files
                    String templateFileRoot = Paths.get(localConfigRoot, "template").toString();
                    File templateRoot = new File(templateFileRoot);
                    if (templateRoot.exists() && templateRoot.isDirectory()) {
                        File[] templateFiles = templateRoot.listFiles();
                        Map<String, String> templateMap = new HashMap<>();
                        for (File templateFile : templateFiles) {
                            String templateFileContent = FileUtil.readFile(templateFile.getPath());
                            templateMap.put(templateFile.getName(), templateFileContent);
                        }
                        config.setTemplates(templateMap);
                    }
                    localConfigValidAndOptimizing(config);
                    return config;
                }
            }
        }
        return null;
    }

    private static void localConfigValidAndOptimizing(Config config) {
        if (config.getExtendColumns() != null && config.getTypeMappings().size() > 0) {
            for (ExtendColumn extendColumn : config.getExtendColumns()) {
                if (StringUtil.isNullOrEmpty(extendColumn.getName())) {
                    throw new RuntimeException("extend column's name can not be null or empty");
                }
                if (StringUtil.isNullOrEmpty(extendColumn.getType())) {
                    throw new RuntimeException("extend column's type can not be null or empty");
                }
                if (Arrays.stream(ExtendColumnTypeEnum.values()).map(i -> i.name()).noneMatch(i -> i.toUpperCase().equals(extendColumn.getType().toUpperCase()))) {
                    throw new RuntimeException("extend column's type can be BOOLEAN or SELECT or INPUT only");
                }

                extendColumn.setType(extendColumn.getType().toUpperCase());
            }
        }
    }

    public static Config getDefaultConfig() throws JAXBException, IOException {
        URL url = ConfigManager.class.getClassLoader().getResource("config/config.xml");
        if (url != null) {
            InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("config/config.xml");
            String fileContent = FileUtil.readInputSteam(inputStream);
            Config config = ObjectUtil.xmlStringToObject(Config.class, fileContent);
            String[] inputStreams = FileUtil.getResources(ConfigManager.class.getClassLoader(), "config/template");
            Map<String, String> tempaltes = new HashMap<>();
            for (String name : inputStreams) {
                InputStream templateName = ConfigManager.class.getClassLoader().getResourceAsStream(name);
                String templateContent = FileUtil.readInputSteam(templateName);
                tempaltes.put(new File(name).getName(), templateContent);
            }
            config.setTemplates(tempaltes);
            return config;
        }
        return null;
    }

    /**
     * if project has config,load it,otherwise load the default config
     *
     * @return
     */
    public static Config getConfig() throws Exception {
        Config localConfig = getLocalConfig();
        if (localConfig != null) {
            return localConfig;
        }
        return getDefaultConfig();
    }

    public static void saveDefaultConfigToLocal() throws JAXBException, IOException {
        Project project = ProjectUtil.getProject();
        if (project == null) {
            Messages.showErrorDialog("there is no project", "Error");
        }
        Config defaultConfig = ConfigManager.getDefaultConfig();
        String basePath = project.getBasePath();
        String localConfigRoot = Paths.get(basePath, relConfigPath).toString();
        // save config file
        String configFileName = Paths.get(localConfigRoot, "config.xml").toString();
        // File configFile = new File(configFileName);
        FileUtil.writeFile(configFileName, ObjectUtil.objectToXmlString(Config.class, defaultConfig));
        // save template files
        String templateFileRoot = Paths.get(localConfigRoot, "template").toString();
        Map<String, String> templateMap = defaultConfig.getTemplates();
        if (templateMap != null) {
            templateMap.forEach((key, value) -> {
                File templateFile = new File(Paths.get(templateFileRoot, key).toUri());
                FileUtil.writeFile(templateFile.getPath(), value);
            });
        }
    }
}
