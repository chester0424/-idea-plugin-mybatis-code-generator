package zone.pusu.mybatisCodeGenerator.tool;

import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;

public class ObjectUtil {
    public static <T> T clone(T source, Type type) {
        return JsonUtil.fromJson(JsonUtil.toJson(source), type);
    }

    public static <T> T clone(T source, TypeToken typeToken) {
        return clone(source, typeToken.getType());
    }

    public static <T> T xmlStringToObject(Class<T> clazz, String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        T t = (T) unmarshaller.unmarshal(reader);
        return t;
    }

    public static <T> String objectToXmlString(Class<T> clazz, Object object) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jaxbContext.createMarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(object, outputStream);
        return outputStream.toString();
    }
}
