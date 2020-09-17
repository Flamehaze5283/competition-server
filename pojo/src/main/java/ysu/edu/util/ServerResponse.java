package ysu.edu.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse {

    @ApiModelProperty(example="200")
    private Integer code;
    @ApiModelProperty(example="data")
    private Object data;
    @ApiModelProperty(example="操作成功")
    private String msg;
}
