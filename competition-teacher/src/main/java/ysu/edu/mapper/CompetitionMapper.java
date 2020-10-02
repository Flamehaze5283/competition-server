package ysu.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ysu.edu.pojo.Competition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2020-09-21
 */
public interface CompetitionMapper extends BaseMapper<Competition> {
    @Select("SELECT i.*,t.`realname` teacher_name FROM competition i LEFT JOIN teacher t ON i.`teacher_id` = t.`teacher_id` ${ew.customSqlSegment}")
    IPage<Competition> list(IPage<Competition> page, @Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT i.*,t.`realname` teacher_name FROM competition i LEFT JOIN teacher t ON i.`teacher_id` = t.`teacher_id` ${ew.customSqlSegment}")
    List<Competition> noPage(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT i.*,t.`realname` teacher_name,l.`name` level_name,r.`name` type_name FROM competition i LEFT JOIN teacher t ON i.`teacher_id` = t.`teacher_id` LEFT JOIN constant_item l ON i.`level` = l.`code` AND l.`type_id` = 4 LEFT JOIN constant_item r ON i.`type` = r.`code` AND r.`type_id` = 5 ${ew.customSqlSegment}")
    IPage<Competition> myGetPage(IPage<Competition> page,@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT i.*,t.`realname` teacher_name,l.`name` level_name,r.`name` type_name FROM competition i LEFT JOIN teacher t ON i.`teacher_id` = t.`teacher_id` LEFT JOIN constant_item l ON i.`level` = l.`code` AND l.`type_id` = 4 LEFT JOIN constant_item r ON i.`type` = r.`code` AND r.`type_id` = 5 ${ew.customSqlSegment}")
    List<Competition> myGetOne(@Param(Constants.WRAPPER) Wrapper wrapper);

}
