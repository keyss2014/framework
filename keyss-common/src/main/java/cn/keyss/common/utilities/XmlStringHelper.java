package cn.keyss.common.utilities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Xml字符串帮助器
 */
public abstract class XmlStringHelper {

    public static <T> String saveToXmlString(Class<T> clazz, Object obj) throws IOException, JAXBException {
        try (StringWriter stringWriter = new StringWriter()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(obj, stringWriter);
            return stringWriter.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadFromXmlString(Class<T> clazz, String xmlObj) throws JAXBException {
        try (StringReader stringReader = new StringReader(xmlObj)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(stringReader);
        }
    }
}
