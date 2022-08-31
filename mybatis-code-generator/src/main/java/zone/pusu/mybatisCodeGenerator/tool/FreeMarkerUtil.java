package zone.pusu.mybatisCodeGenerator.tool;

import freemarker.template.Configuration;
import freemarker.template.Template;
import zone.pusu.mybatisCodeGenerator.common.MCGException;

import java.io.StringWriter;

/**
 * 模板生成
 */
public class FreeMarkerUtil {
    /**
     * 模板处理数据
     *
     * @param fileName
     * @param data
     * @return
     */
    public static String process(String fileName, Object data) {
        try {
            Configuration configuration = new Configuration(Configuration.getVersion());
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(FreeMarkerUtil.class, "/template/");
            Template template = configuration.getTemplate(fileName);
            StringWriter sw = new StringWriter();
            template.process(data, sw);
            return new String(sw.getBuffer());
        } catch (Exception exception) {
            throw new MCGException(exception.getMessage());
        }
    }

}
