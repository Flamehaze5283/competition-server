package ysu.edu.service.impl;

import ysu.edu.pojo.Competition;
import ysu.edu.mapper.CompetitionMapper;
import ysu.edu.service.ICompetitionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zh
 * @since 2020-09-21
 */
@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapper, Competition> implements ICompetitionService {

    @Override
    public boolean add(Competition competition) {
        competition.setActive(1);
        competition.setCreateTime(LocalDateTime.now());
        return save(competition);
    }
}
