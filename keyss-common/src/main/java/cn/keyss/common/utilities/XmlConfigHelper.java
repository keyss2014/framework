package cn.keyss.common.utilities;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

/**
 * XML配置文件帮助器
 */
public class XmlConfigHelper {
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * 相对根目录，加载配置文件
     *
     * @param clazz 配置类型
     * @param url   文件路径
     * @param <T>   配置类型
     * @return 配置
     * @throws JAXBException
     * @throws IOException
     */
    public static <T> T loadConfig(Class<T> clazz, String url) throws JAXBException, IOException {
        Resource resource   =resourceLoader.getResource(url) ;
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (T) jaxbUnmarshaller.unmarshal(resource.getInputStream());
    }
}