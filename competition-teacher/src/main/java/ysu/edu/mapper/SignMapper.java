package ysu.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ysu.edu.pojo.Student;

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

    @Select("SELECT i.*,k.`name` student_name,r.`name` verify_name FROM sign i LEFT JOIN student k ON i.`student_id` = k.`num_id` LEFT JOIN constant_item r ON i.`verify` = r.`code` AND r.`type_id` = 3 ${ew.customSqlSegment}")
    List<Sign> getList(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT i.*,c.`name` verify_name,s.`name` captain_name FROM sign i LEFT JOIN constant_item c ON i.`verify` = c.`code` AND c.`type_id` = 3 LEFT JOIN student s ON i.`captain_id` = s.`num_id` ${ew.customSqlSegment}")
    List<Sign> theTeamList(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT name as student_name,num_id from student ${ew.customSqlSegment}")
    List<Sign> theTeam2List(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<Sign> selfList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT name FROM student ${ew.customSqlSegment}")
    Student stuName(@Param(Constants.WRAPPER) Wrapper wrapper);
}
