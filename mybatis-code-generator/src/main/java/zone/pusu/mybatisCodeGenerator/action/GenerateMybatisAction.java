package zone.pusu.mybatisCodeGenerator.action;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import zone.pusu.mybatisCodeGenerator.common.MCGException;
import zone.pusu.mybatisCodeGenerator.define.*;
import zone.pusu.mybatisCodeGenerator.tool.FileUtil;
import zone.pusu.mybatisCodeGenerator.tool.FreeMarkerUtil;
import zone.pusu.mybatisCodeGenerator.tool.JsonUtil;
import zone.pusu.mybatisCodeGenerator.tool.StringUtil;
import zone.pusu.mybatisCodeGenerator.ui.MyMainFrame;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateMybatisAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            ClassInfo classInfo = analysisCurrentJavaFile(e);

            GenerateMybatisConfigClass configClass = getGenerateMybatisConfigClass(classInfo);

            MyMainFrame myMainFrame = new MyMainFrame(configClass);
            myMainFrame.addOperateListener((operate, params) -> {
                if (operate == MyMainFrame.Operate_Save) {
                    saveConfigInfo(classInfo, configClass);
                    Messages.showInfoMessage("has been Saved", "Info");
                } else if (operate == MyMainFrame.Operate_Generate) {
                    if (params != null) {
                        // 默认路径 和文件名
                        String fileDir = Paths.get(new File(classInfo.getFilePath()).getParent(), "dao").toString();
                        String fileName = "I" + classInfo.getName() + "Dao.java";
                        TempDataContent tempDataContent = new TempDataContent(configClass, fileDir, fileName);

                        String content = FreeMarkerUtil.process("dao.ftl", tempDataContent);
                        String filePath = Paths.get(tempDataContent.getFileDir(), tempDataContent.getFileName()).toString();
                        FileUtil.writeFile(filePath, content);
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
            throw new MCGException("not support readOnly file");
        }
        if (!psiFile.getVirtualFile().getName().contains(".java")) {
            throw new MCGException("not support except java file");
        }
        ClassInfo classInfo = new ClassInfo();
        PsiJavaFileImpl psiJavaFile = new PsiJavaFileImpl(psiFile.getViewProvider());
        List<PsiElement> psiClassList = Arrays.stream(psiJavaFile.getChildren()).filter(PsiClass.class::isInstance).collect(Collectors.toList());
        if (psiClassList.size() > 0) {
            PsiClass psiClass = (PsiClass) psiClassList.get(0);
            if (!psiClass.getName().equals(psiFile.getName().substring(0, psiFile.getName().indexOf(".")))) {
                throw new MCGException("class name and file name area not the same");
            }
            if (!psiClass.isValid()) {
                throw new MCGException("class is not valid");
            }

            PsiField[] psiFields = psiClass.getFields();

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
            throw new MCGException("there is not class");
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
                    if (field.isQuery()) {
                        optional.get().setQuery(field.isQuery());
                    }
                    if (field.isOrderBy()) {
                        optional.get().setOrderBy(field.isOrderBy());
                    }
                }
            }
        }
        return result;
    }

    private String getJdbcTypeByJavaType(String javaType) {
        return "VARCHAR";
    }

    private void saveConfigInfo(ClassInfo classInfo, GenerateMybatisConfigClass configClass) {
        String fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), "dao", classInfo.getName() + "-mcfg.json").toString();
        String content = JsonUtil.toJsonPretty(configClass);
        FileUtil.writeFile(fileName, content);
    }

    private void saveDaoFile(ClassInfo classInfo, GenerateMybatisConfigClass configClass) {
        String fileName = Paths.get(new File(classInfo.getFilePath()).getParent(), "dao", classInfo.getName() + ".java").toString();
        String content = JsonUtil.toJsonPretty(configClass);
        FileUtil.writeFile(fileName, content);
    }

    private ClassInfo getClassInfo(AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if (psiFile.getFileType().isReadOnly()) {
            throw new MCGException("not support readOnly file");
        }
        if (!psiFile.getVirtualFile().getName().contains(".java")) {
            throw new MCGException("not support except java file");
        }


        ClassInfo classInfo = new ClassInfo();

        PsiJavaFileImpl psiJavaFile = new PsiJavaFileImpl(psiFile.getViewProvider());
        List<PsiElement> psiClassList = Arrays.stream(psiJavaFile.getChildren()).filter(PsiClass.class::isInstance).collect(Collectors.toList());
        if (psiClassList.size() > 0) {
            PsiClass psiClass = (PsiClass) psiClassList.get(0);

            if (!psiClass.getName().equals(psiFile.getName().substring(0, psiFile.getName().indexOf(".")))) {
                throw new MCGException("class name and file name area not the same");
            }
            if (!psiClass.isValid()) {
                throw new MCGException("class is not valid");
            }

            PsiField[] psiFields = psiClass.getFields();


            classInfo.setName(psiClass.getName());
            // classInfo.setPackageName(psiClass.ge);
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
        }


        GenerateMybatisConfigClass generateMybatisConfigClass = new GenerateMybatisConfigClass();
        generateMybatisConfigClass.setClassName("Product");

        try {
            String temp = generateDaoContent("dao.ftl", generateMybatisConfigClass);
        } catch (Exception e) {
            throw new MCGException(e.getMessage());
        }

        return null;
    }

    private GenerateMybatisConfigClass getConfig(ClassInfo classInfo) {
        File ioFile = new File("./io.java");
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(ioFile);
        virtualFile.refresh(false, true);
        return null;

    }

    private void saveConfig(GenerateMybatisConfigClass generateMybatisConfigClass, String parentDirectory) {
        String fileName = generateMybatisConfigClass.getClassName() + ".emc";
        Path filePath = Paths.get(parentDirectory, fileName);
        //check file exist
        File configFile = new File(filePath.toString());
        if (configFile.exists()) {
            configFile.delete();
        }
        String jsonStr = new Gson().toJson(generateMybatisConfigClass);
        Writer write = null;
        try {
            write = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            write.write(jsonStr);
            write.flush();
            write.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateFile(GenerateMybatisConfigClass generateMybatisConfigClass) {

    }


    private Map<String, JDBCType> javaTypeJdbcTypeMapping() {
        Map<String, JDBCType> map = new HashMap<>();
        map.put("java.lang.String", JDBCType.VARCHAR);
        map.put("java.lang.byte[]", JDBCType.BLOB);
        map.put("java.lang.Long", JDBCType.INTEGER);
        map.put("java.lang.Integer", JDBCType.INTEGER);
        map.put("java.lang.Boolean", JDBCType.BIT);
        map.put("java.math.BigInteger", JDBCType.BIGINT);
        map.put("java.lang.Float", JDBCType.FLOAT);
        map.put("java.lang.Double", JDBCType.DOUBLE);
        map.put("java.math.BigDecimal", JDBCType.DECIMAL);
        map.put("java.sql.Date", JDBCType.DATE);
        map.put("java.sql.Time", JDBCType.TIME);
        map.put("java.sql.Timestamp", JDBCType.TIMESTAMP);
        return map;
    }

    private String generateDaoContent(String fileName, GenerateMybatisConfigClass data) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        //File file = new File("classpath:template/"+ fileName) ;//this.getClass().getResource() //ResourceUtils.getFile("classpath:lginfo/cppinfo.json");
//        configuration.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/template").getFile()));
        // this.getClass().getResource("template/"+fileName);


        // configuration.setDirectoryForTemplateLoading(new File("D:\\home"));
//        configuration.setDefaultEncoding("utf-8");
        // Template template = configuration.getTemplate(this.getClass().getResource("/template/"+fileName).getFile());
        // Template template = configuration.getTemplate("dao.ftl");
//        StringWriter sw = new StringWriter();
//        template.process(data, sw);
//        return new String(sw.getBuffer());

        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/template/");
        Template template = configuration.getTemplate("dao.ftl");
        StringWriter sw = new StringWriter();
        template.process(data, sw);
        return new String(sw.getBuffer());
    }
}
