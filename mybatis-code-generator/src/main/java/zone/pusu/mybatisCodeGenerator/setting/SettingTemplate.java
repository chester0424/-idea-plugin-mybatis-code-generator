package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

public class SettingTemplate {

    private List<SettingTemplateItem> items = new ArrayList<>();

    public SettingTemplate() {
        for (int i = 0; i < 5; i++) {
            SettingTemplateItem item = new SettingTemplateItem();
            item.setName("Dao" + i);
            item.setContent("Content" + i);
            items.add(item);
        }
    }

    public List<SettingTemplateItem> getItems() {
        return items;
    }

    public void setItems(List<SettingTemplateItem> items) {
        this.items = items;
    }
}

