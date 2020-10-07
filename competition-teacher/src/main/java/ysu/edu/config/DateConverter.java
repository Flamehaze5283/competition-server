package ysu.edu.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 项目    ： his-server
 * 创建时间 ：2020/8/26  9:38 26
 * author  ：jshand
 * site    :  http://314649444.iteye.com
 * 描述     :
 */
@Component
public class DateConverter implements Converter<String, Date> {
    //2020-08-26%2001:02:03
    static List<SimpleDateFormat> formatter = new ArrayList();
    static{
        formatter.add(new SimpleDateFormat("yyyy-MM-dd"));
        formatter.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        formatter.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formatter.add(new SimpleDateFormat("yyyy/MM/dd"));

    }


    @Override
    public Date convert(String source) {
        try {
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2}",source)){
                return formatter.get(0).parse(source);
            }
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}",source)){
                return formatter.get(1).parse(source);
            }
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",source)){
                return formatter.get(2).parse(source);
            }
            if(Pattern.matches("\\d{4}/\\d{2}/\\d{2}",source)){
                return formatter.get(2).parse(source);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}