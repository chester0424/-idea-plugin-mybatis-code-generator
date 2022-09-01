package zone.pusu.mybatisCodeGenerator.setting;

import java.util.ArrayList;
import java.util.List;

public class SettingTemplate {

    private List<SettingTemplateItem> itemList = new ArrayList<>();

    public SettingTemplate() {
        for (int i = 0; i < 5; i++) {
            SettingTemplateItem item = new SettingTemplateItem();
            item.setName("Dao" + i);
            item.setContent("Content" + i);
            itemList.add(item);
        }
    }

    public List<SettingTemplateItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SettingTemplateItem> itemList) {
        this.itemList = itemList;
    }
}

