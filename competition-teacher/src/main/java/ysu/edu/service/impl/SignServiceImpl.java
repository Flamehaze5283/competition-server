package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ysu.edu.pojo.Sign;
import ysu.edu.mapper.SignMapper;
import ysu.edu.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

    @Override
    public Object list(Sign sign) {
        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id",sign.getCompetitionId());
        return getBaseMapper().list(new Page<>(sign.getPageNo(),sign.getPageSize()),wrapper);
    }
}
