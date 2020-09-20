package ysu.edu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 页号
    @JsonIgnore
    @TableField(exist = false)
    private int pageSize = 5;

    // 每页大小
    @JsonIgnore
    @TableField(exist = false)
    private int pageNum = 1;

    // 是否分页  1分页 0不分
    @JsonIgnore
    @TableField(exist = false)
    private Integer withPage = 1;
}
