package zone.pusu.mybatisCodeGenerator.tool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.Nullable;

/**
 * project util
 */
public class ProjectUtil {
    public static @Nullable Project getProject() {
        ProjectManager projectManager = ProjectManager.getInstanceIfCreated();
        if (projectManager != null) {
            Project[] projects = projectManager.getOpenProjects();
            if (projects.length > 0) {
                return projects[0];
            }
        }
        return null;
    }
}
