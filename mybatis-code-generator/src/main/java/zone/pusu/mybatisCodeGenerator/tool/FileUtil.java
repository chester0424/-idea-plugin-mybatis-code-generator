package zone.pusu.mybatisCodeGenerator.tool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import zone.pusu.mybatisCodeGenerator.common.MCGException;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

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

    public static String readResourceText(File file) {
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String readInputSteam(InputStream stream) throws IOException {
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

    /**
     * 获取jar包中目录下的文件
     *
     * @param classLoader
     * @param dirPath
     * @return
     * @throws IOException
     */
    public static String[] getResources(ClassLoader classLoader, String dirPath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        URL url = classLoader.getResource(dirPath);
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        Enumeration<JarEntry> jarEntryEnumeration = jarURLConnection.getJarFile().entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String name = jarEntry.getName();
            if (name.startsWith(dirPath) && !jarEntry.isDirectory()) {
                fileNames.add(name);
            }
        }
        return fileNames.toArray(new String[]{});
    }
}
