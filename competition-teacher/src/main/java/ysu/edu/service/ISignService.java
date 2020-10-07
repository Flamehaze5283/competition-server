package ysu.edu.service;

import ysu.edu.pojo.Sign;
import com.baomidou.mybatisplus.extension.service.IService;
import ysu.edu.util.TeamDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface ISignService extends IService<Sign> {
    Object list(Sign sign);
    Object selfList(Sign sign);
    Object teamList(Sign sign);
    TeamDTO overList(Sign sign, String[] students);
    Object getList(Sign sign);
    boolean submit(String str,String active);
    Object detailSelfList(Sign sign);
}
