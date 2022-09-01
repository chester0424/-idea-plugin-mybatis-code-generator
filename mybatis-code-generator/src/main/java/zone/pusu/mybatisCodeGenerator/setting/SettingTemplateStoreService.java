package zone.pusu.mybatisCodeGenerator.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "zone.pusu.mybatisCodeGenerator.setting.SettingTemplateStoreService", storages = @Storage("setting-template.xml"))
public class SettingTemplateStoreService implements PersistentStateComponent<SettingTemplate> {

    SettingTemplate settingTemplate = new SettingTemplate();

    public static SettingTemplateStoreService getInstance() {
        return ServiceManager.getService(SettingTemplateStoreService.class);
    }

    @Override
    public @Nullable SettingTemplate getState() {
        return settingTemplate;
    }

    @Override
    public void loadState(@NotNull SettingTemplate state) {
        settingTemplate = state;
    }
}
