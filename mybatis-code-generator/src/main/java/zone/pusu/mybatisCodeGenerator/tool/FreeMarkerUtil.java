package zone.pusu.mybatisCodeGenerator.tool;

import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import zone.pusu.mybatisCodeGenerator.common.MCGException;

import java.io.StringWriter;

/**
 * 模板生成
 */
public class FreeMarkerUtil {
    /**
     * 模板处理
     *
     * @param templateContent 模板内容
     * @param context         上下文数据
     * @return
     */
    public static String process(String tempFileName, String templateContent, Object context) {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
            configuration.setDefaultEncoding("utf-8");
            final ByteArrayTemplateLoader templateLoader = new ByteArrayTemplateLoader();
            templateLoader.putTemplate(tempFileName, templateContent.getBytes());
            configuration.setTemplateLoader(templateLoader);
            Template template = configuration.getTemplate(tempFileName);
            StringWriter sw = new StringWriter();
            template.process(context, sw);
            return new String(sw.getBuffer());
        } catch (Exception exception) {
            throw new MCGException(exception.getMessage());
        }
    }
}
