package zone.pusu.mybatisCodeGenerator.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "zone.pusu.mybatisCodeGenerator.setting.SettingMainStoreService", storages = @Storage("setting-main.xml"))
public class SettingMainStoreService implements PersistentStateComponent<SettingMain> {

    private SettingMain settingMain = new SettingMain();

    public static SettingMainStoreService getInstance() {
        return ApplicationManager.getApplication().getService(SettingMainStoreService.class);
    }

    @Override
    public @NotNull SettingMain getState() {
        return settingMain;
    }

    @Override
    public void loadState(@NotNull SettingMain state) {
        settingMain = state;
    }
}
