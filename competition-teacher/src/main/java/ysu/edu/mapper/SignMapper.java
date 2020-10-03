package ysu.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface SignMapper extends BaseMapper<Sign> {
    @Select("SELECT i.*,t.`name` student_name,r.`name` verify_name FROM sign i LEFT JOIN student t ON i.`competition_id` = t.`id` LEFT JOIN constant_item r ON i.`verify` = r.`code` AND r.`type_id` = 3 ${ew.customSqlSegment}")
    IPage<Sign> list(IPage<Competition> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<Sign> selfList(@Param(Constants.WRAPPER) Wrapper wrapper);
}
