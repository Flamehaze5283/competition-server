package ysu.edu.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignFormDTO {
    String parts;
    List<String> activeParams;
}
