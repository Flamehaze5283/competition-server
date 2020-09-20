package ysu.edu.pojo;

import lombok.Data;

@Data
public class Email {
    public static final String from = "actividong@qq.com";
    public static final String personal = "1124792103";

    private String to;
    private String subject;
    private String text;
}