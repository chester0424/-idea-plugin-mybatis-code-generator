package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

public class SettingTemplate {

    private List<SettingTemplateItem> items = new ArrayList<>();

    public List<SettingTemplateItem> getItems() {
        return items;
    }

    public void setItems(List<SettingTemplateItem> items) {
        this.items = items;
    }
}

