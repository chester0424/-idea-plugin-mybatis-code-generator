package zone.pusu.mybatisCodeGenerator.define;

public class TemplateDataContextCallBack {
    TemplateDataContext templateDataContext;

    public TemplateDataContextCallBack(TemplateDataContext templateDataContext) {
        this.templateDataContext = templateDataContext;
    }

    public void setTargetFileDir(String targetFileDir) {
        templateDataContext.put("targetFileDir", targetFileDir);
    }

    public void setTargetFileName(String targetFileName) {
        templateDataContext.put("targetFileName", targetFileName);
    }
}
