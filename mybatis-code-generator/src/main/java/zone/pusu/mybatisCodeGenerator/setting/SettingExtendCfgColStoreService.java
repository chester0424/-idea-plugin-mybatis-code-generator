package zone.pusu.mybatisCodeGenerator.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "zone.pusu.mybatisCodeGenerator.setting.SettingExtendCfgColStoreService", storages = @Storage("setting-extend-cfg-col.xml"))
public class SettingExtendCfgColStoreService implements PersistentStateComponent<SettingExtendCfgCol> {

    private SettingExtendCfgCol settingExtendCfgCol = new SettingExtendCfgCol();

    public static SettingExtendCfgColStoreService getInstance() {
        return ApplicationManager.getApplication().getService(SettingExtendCfgColStoreService.class);
    }

    @Override
    public @Nullable SettingExtendCfgCol getState() {
        return settingExtendCfgCol;
    }

    @Override
    public void loadState(@NotNull SettingExtendCfgCol state) {
        settingExtendCfgCol = state;
    }
}
