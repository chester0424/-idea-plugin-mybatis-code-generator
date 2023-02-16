package zone.pusu.mybatisCodeGenerator.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiClassUtil;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.define.ClassInfo;
import zone.pusu.mybatisCodeGenerator.define.FieldInfo;
import zone.pusu.mybatisCodeGenerator.ui.CodeGenerateMainFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateMybatisAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Project project = e.getProject();
            ClassInfo classInfo = analysisCurrentJavaFile(e);
            new CodeGenerateMainFrame(classInfo);
        } catch (Exception exception) {
            Messages.showErrorDialog(exception.getMessage(), "Error");
        }
    }

    private ClassInfo analysisCurrentJavaFile(AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if (psiFile.getFileType().isReadOnly()) {
            throw new MCGException("The current file cannot be read-only");
        }
        if (!psiFile.getVirtualFile().getName().contains(".java")) {
            throw new MCGException("The current file does not support this operation (only java files)");
        }
        ClassInfo classInfo = new ClassInfo();
        PsiJavaFileImpl psiJavaFile = new PsiJavaFileImpl(psiFile.getViewProvider());
        List<PsiElement> psiClassList = Arrays.stream(psiJavaFile.getChildren()).filter(PsiClass.class::isInstance).collect(Collectors.toList());
        if (psiClassList.size() > 0) {
            PsiClass psiClass = (PsiClass) psiClassList.get(0);
            if (!psiClass.getName().equals(psiFile.getName().substring(0, psiFile.getName().indexOf(".")))) {
                throw new MCGException("Class and file must have the same name");
            }
            if (!psiClass.isValid()) {
                throw new MCGException("The class is not valid");
            }

            classInfo.setName(psiClass.getName());
            classInfo.setFilePath(psiFile.getVirtualFile().getPath());
            classInfo.setPackageName(psiJavaFile.getPackageName());
            classInfo.setFileName(psiFile.getName());
            classInfo.setFieldInfos(new ArrayList<>());
            for (PsiField field : psiClass.getFields()) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setName(field.getName());
                fieldInfo.setType(field.getType().getCanonicalText());
                classInfo.getFieldInfos().add(fieldInfo);
            }
            return classInfo;
        } else {
            throw new MCGException("There is no class");
        }
    }
}
