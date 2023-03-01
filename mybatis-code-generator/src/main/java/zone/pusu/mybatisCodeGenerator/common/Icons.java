package zone.pusu.mybatisCodeGenerator.common;

import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

public class Icons {
    public static final @NotNull javax.swing.Icon PluginIcon = load("META-INF/pluginIcon.svg");

    private static @NotNull javax.swing.Icon load(@NotNull String path) {
        return IconManager.getInstance().getIcon(path, Icons.class);
    }
}
