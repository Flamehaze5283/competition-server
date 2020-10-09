package ysu.edu.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Resource
    MyWebInterceptor myWebInterceptor;
    @Resource
    LocalDateTimeConverter localDateTimeConverter;
    @Resource
    LocalDateConverter localDateConverter;
    @Resource
    DateConverter dateConverter;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myWebInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/teacher/token",
                        "/teacher/logout",
                        "/teacher/rePassword",
                        "/download/person",
                        "/download/team"
                );
    }
    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(localDateTimeConverter);
        registry.addConverter(localDateConverter);
        registry.addConverter(dateConverter);
    }
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        objectMapper.registerModule(javaTimeModule);
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
