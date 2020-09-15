package ysu.edu.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntry {
    private Integer id;
    private LocalDateTime createTime;
}
