package ysu.edu.service;

import ysu.edu.pojo.ConstantType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 常数类别 服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
public interface IConstantTypeService extends IService<ConstantType> {
    Object list(ConstantType constantType);
    boolean add(ConstantType constantType);
    boolean batchdel(Integer[] ids);
}
