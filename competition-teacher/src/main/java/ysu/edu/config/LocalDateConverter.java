package ysu.edu.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 项目    ： his-server
 * 创建时间 ：2020/8/26  9:29 26
 * author  ：jshand
 * site    :  http://314649444.iteye.com
 * 描述     :
 */
@Component
public class LocalDateConverter implements Converter<String, LocalDate> {
    //2020-08-26%2001:02:03
    @Override
    public LocalDate convert(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
