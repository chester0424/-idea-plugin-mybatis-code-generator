package zone.pusu.mybatisCodeGenerator.tool;

import zone.pusu.mybatisCodeGenerator.common.MCGException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件操作
 */
public class FileUtil {
    /**
     * 读取文件，如果文件不存在，返回 null，
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                String data = new String(Files.readAllBytes(Paths.get(fileName)));
                return data;
            }
        } catch (Exception e) {
            throw new MCGException(e.getMessage());
        }
        return null;
    }

    public static void writeFile(String fileName, String content) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception exception) {
            throw new MCGException(exception.getMessage());
        }
    }
}
