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
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.define.*;
import zone.pusu.mybatisCodeGenerator.setting.*;
import zone.pusu.mybatisCodeGenerator.tool.FileUtil;
import zone.pusu.mybatisCodeGenerator.tool.FreeMarkerUtil;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;
import zone.pusu.mybatisCodeGenerator.ui.CodeGenerateMainFrame;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenerateMybatisAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Project project = e.getProject();
            ClassInfo classInfo = analysisCurrentJavaFile(e);

            GenerateMybatisConfigClass configClass = getGenerateMybatisConfigClass(classInfo);

            CodeGenerateMainFrame codeGenerateMainFrame = new CodeGenerateMainFrame(configClass);
            codeGenerateMainFrame.addOperateListener((operate, params) -> {
                if (operate == CodeGenerateMainFrame.Operate_Save) {
                    saveConfigInfo(classInfo, configClass);
                    Messages.showInfoMessage("Saved", "Info");
                } else if (operate == CodeGenerateMainFrame.Operate_Generate) {
                    if (params == null || params.length == 0) {
                        Messages.showInfoMessage("Firstly,select a file needed to be generated", "Notice");
                        return;
                    }
                    for (String templateName : params) {
                        for (SettingTemplateItem item : SettingTemplateStoreService.getInstance().getState().getItems()) {
                            if (item.getName().equals(templateName)) {
                                // 默认路径和名称
                                String fileDir = Paths.get(new File(classInfo.getFilePath()).getParent()).toString();
                                String fileName = configClass.getClassName() + templateName;
                                TempDataContext tempDataContext = new TempDataContext(configClass, fileDir, fileName);
                                String content = FreeMarkerUtil.process(item.getContent(), tempDataContext);
                                String filePath = Paths.get(tempDataContext.getFileDir(), tempDataContext.getFileName()).toString();
                                FileUtil.writeFile(filePath, content);
                                Messages.showInfoMessage("Generate file Successfully","Notice");
                            }
                        }
                    }

                } else {
                    throw new MCGException("不支持");
                }
            });
        } catch (Exception exception) {
            Messages.showErrorDialog(exception.getMessage(), "Error");
        }
    }

    private ClassInfo analysisCurrentJavaFile(AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if (psiFile.getFileType().isReadOnly()) {
            throw new MCGException("don't support readOnly file");
        }
        if (!psiFile.getVirtualFile().getName().contains(".java")) {
            throw new MCGException("don't support file except java");
        }
        ClassInfo classInfo = new ClassInfo();
        PsiJavaFileImpl psiJavaFile = new PsiJavaFileImpl(psiFile.getViewProvider());
        List<PsiElement> psiClassList = Arrays.stream(psiJavaFile.getChildren()).filter(PsiClass.class::isInstance).collect(Collectors.toList());
        if (psiClassList.size() > 0) {
            PsiClass psiClass = (PsiClass) psiClassList.get(0);
            if (!psiClass.getName().equals(psiFile.getName().substring(0, psiFile.getName().indexOf(".")))) {
                throw new MCGException("class and file must be the same name");
            }
            if (!psiClass.isValid()) {
                throw new MCGException("class is not valid");
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
            throw new MCGException("there is no class");
        }
    }

    private GenerateMybatisConfigClass getGenerateMybatisConfigClass(ClassInfo classInfo) {
        GenerateMybatisConfigClass result = new GenerateMybatisConfigClass();
        // 通过classInfo 构造配置信息
        result.setClassName(classInfo.getName());
        result.setPackageName(classInfo.getPackageName());
        result.setTableName(classInfo.getName()); // 默认把对象名设置为表名
        result.setFields(new ArrayList<>());
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            GenerateMybatisConfigField field = new GenerateMybatisConfigField();
            field.setName(fieldInfo.getName());
            field.setJavaType(fieldInfo.getType());
            field.setJdbcType(getJdbcTypeByJavaType(fieldInfo.getType()));
            result.getFields().add(field);
        }

        // 文件中读取配置信息
        String fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), "dao", classInfo.getName() + "-mcfg.json").toString();//  + "/dao/" + classInfo.getName() + "-mcfg.json";
        String content = FileUtil.readFile(fileName);
        if (content != null) {
            GenerateMybatisConfigClass configClassFromConfigFile = JsonUtil.fromJson(content, GenerateMybatisConfigClass.class);
            if (!StringUtil.isNullOrEmpty(configClassFromConfigFile.getTableName())) {
                result.setTableName(configClassFromConfigFile.getTableName());
            }
            for (GenerateMybatisConfigField field : configClassFromConfigFile.getFields()) {
                Optional<GenerateMybatisConfigField> optional = result.getFields().stream().filter(i -> i.getName().equals(field.getName()) && i.getJavaType().equals(field.getJavaType())).findFirst();
                if (optional.isPresent()) {
                    if (StringUtil.isNullOrEmpty(field.getJdbcType())) {
                        optional.get().setJdbcType(field.getJdbcType());
                    }
                    if (field.isPrimaryKey()) {
                        optional.get().setPrimaryKey(field.isPrimaryKey());
                    }
                }
            }
        }
        return result;
    }

    private String getJdbcTypeByJavaType(String javaType) {
        Optional<SettingTypeMappingItem> optional = SettingTypeMappingStoreService.getInstance().getState().getItems().stream().filter(i -> i.getJavaType().equals(javaType)).findFirst();
        if (optional.isPresent()) {
            return optional.get().getJdbcType();
        }
        return "";
    }

    private void saveConfigInfo(ClassInfo classInfo, GenerateMybatisConfigClass configClass) {
        String fileName = classInfo.getName() + "-mcfg.json";
        String settingConfigFileSavePath = SettingMainStoreService.getInstance().getState().getConfigFileSavePath();
        if (!StringUtil.isNullOrEmpty(settingConfigFileSavePath) && new File(settingConfigFileSavePath).isAbsolute()) {
            fileName = Paths.get(settingConfigFileSavePath, fileName).toString();
        } else {
            fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), settingConfigFileSavePath, fileName).toString();
        }
        String content = JsonUtil.toJsonPretty(configClass);
        FileUtil.writeFile(fileName, content);
    }
}
