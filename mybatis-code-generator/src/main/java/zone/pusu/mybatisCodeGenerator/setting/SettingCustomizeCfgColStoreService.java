package zone.pusu.mybatisCodeGenerator.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "zone.pusu.mybatisCodeGenerator.setting.SettingCustomizeCfgColStoreService", storages = @Storage("setting-customize-cfg-col.xml"))
public class SettingCustomizeCfgColStoreService implements PersistentStateComponent<SettingCustomizeCfgCol> {

    private SettingCustomizeCfgCol settingCustomizeCfgCol = new SettingCustomizeCfgCol();

    public static SettingCustomizeCfgColStoreService getInstance() {
        return ServiceManager.getService(SettingCustomizeCfgColStoreService.class);
    }

    @Override
    public @Nullable SettingCustomizeCfgCol getState() {
        return settingCustomizeCfgCol;
    }

    @Override
    public void loadState(@NotNull SettingCustomizeCfgCol state) {
        settingCustomizeCfgCol = state;
    }
}
