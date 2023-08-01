package zone.pusu.mybatisCodeGenerator.common;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Icons {
    public static final @NotNull javax.swing.Icon PluginIcon = load("META-INF/pluginIcon.svg");

    private static @NotNull javax.swing.Icon load(@NotNull String path) {
        return Objects.requireNonNull(IconLoader.findIcon(path));
    }
}
