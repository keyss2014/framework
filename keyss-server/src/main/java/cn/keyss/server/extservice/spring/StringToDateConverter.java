package cn.keyss.server.extservice.spring;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 和ExtJs日期数据转换器
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static SimpleDateFormat shortFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        if (source == null)
            return null;
        String newSource = source.trim();
        if (newSource.equals(""))
            return null;

        int length = newSource.length();
        try {
            switch (length) {
                case 10:
                    return shortFormatter.parse(newSource);
                case 19:
                    return longFormatter.parse(newSource);
                default:
                    return SimpleDateFormat.getInstance().parse(newSource);
            }
        } catch (ParseException ex) {
            return null;
        }
    }
}