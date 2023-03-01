package zone.pusu.mybatisCodeGenerator.setting;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SettingTemplate {

    private List<SettingTemplateItem> items = new ArrayList<>();

    public SettingTemplate() {
        this.items = getDefaultTemplate();
    }

    public List<SettingTemplateItem> getItems() {
        return items;
    }

    public void setItems(List<SettingTemplateItem> items) {
        this.items = items;
    }

    private List<SettingTemplateItem> getDefaultTemplate() {
        List<SettingTemplateItem> builtInTemplateItemList = new ArrayList<>();
        String[] templateFileNames = new String[]{
                "Dao.java.txt",
                "Mapper.xml.txt",
                "QueryParam.java.txt",
                "Sql.sql.txt"
        };
        for (String templateFileName : templateFileNames) {
            URL url = this.getClass().getClassLoader().getResource("template/" + templateFileName);
            String template_content = "";
            try {
                template_content = new String((byte[]) url.getContent(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                continue;
            }
            String template_name = templateFileName.substring(0, templateFileName.lastIndexOf("."));

            SettingTemplateItem settingTemplateItem = new SettingTemplateItem();
            settingTemplateItem.setName(template_name);
            settingTemplateItem.setContent(template_content);
            builtInTemplateItemList.add(settingTemplateItem);
        }
        return builtInTemplateItemList;
    }
}

