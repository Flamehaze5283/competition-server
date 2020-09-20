package ysu.edu.service.impl;

import ysu.edu.pojo.ConstantItem;
import ysu.edu.mapper.ConstantItemMapper;
import ysu.edu.service.IConstantItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 常数项表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class ConstantItemServiceImpl extends ServiceImpl<ConstantItemMapper, ConstantItem> implements IConstantItemService {

}
