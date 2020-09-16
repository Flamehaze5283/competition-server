package ysu.edu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonIgnore
    @TableField(exist = false)
    private int pageSize = 5;

    @JsonIgnore
    @TableField(exist = false)
    private int pageNum = 1;

    @JsonIgnore
    @TableField(exist = false)
    private boolean withPage = true;
}
