package zone.pusu.mybatisCodeGenerator.common;

import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

public class Icons {
    public static final @NotNull javax.swing.Icon PluginIcon = load("META-INF/pluginIcon.svg", 7547907995386870424L, 0);

    private static @NotNull javax.swing.Icon load(@NotNull String path, long cacheKey, int flags) {
        return IconManager.getInstance().loadRasterizedIcon(path, Icons.class.getClassLoader(), cacheKey, flags);
    }
}
