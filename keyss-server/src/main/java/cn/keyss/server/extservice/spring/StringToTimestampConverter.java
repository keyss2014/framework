package cn.keyss.server.extservice.spring;


import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 和ExtJs日期数据转换器
 */
public class StringToTimestampConverter implements Converter<String, Timestamp> {

    private static SimpleDateFormat shortFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Timestamp convert(String source) {
        if (source == null)
            return null;
        String newSource = source.trim();
        if (newSource.equals(""))
            return null;

        int length = newSource.length();
        try {
            switch (length) {
                case 10:
                    return new Timestamp(shortFormatter.parse(newSource).getTime());
                case 19:
                    return new Timestamp(longFormatter.parse(newSource).getTime());
                default:
                    return Timestamp.valueOf(newSource);
            }
        } catch (ParseException ex) {
            return null;
        }
    }
}