package zone.pusu.mybatisCodeGenerator.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "zone.pusu.mybatisCodeGenerator.setting.SettingTypeMappingStoreService", storages = @Storage("setting-type-mapping.xml"))
public class SettingTypeMappingStoreService implements PersistentStateComponent<SettingTypeMapping> {

    private SettingTypeMapping typeMapping = new SettingTypeMapping();

    public static SettingTypeMappingStoreService getInstance() {
        return ServiceManager.getService(SettingTypeMappingStoreService.class);
    }

    @Override
    public @Nullable SettingTypeMapping getState() {
        return typeMapping;
    }

    @Override
    public void loadState(@NotNull SettingTypeMapping state) {
        typeMapping = state;
    }

}
