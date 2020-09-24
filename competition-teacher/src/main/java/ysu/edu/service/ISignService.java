package ysu.edu.service;

import ysu.edu.pojo.Sign;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
