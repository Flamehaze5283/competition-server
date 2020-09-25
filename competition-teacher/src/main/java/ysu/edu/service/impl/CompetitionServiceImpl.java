package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ysu.edu.pojo.Competition;
import ysu.edu.mapper.CompetitionMapper;
import ysu.edu.service.ICompetitionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import ysu.edu.service.IUploadService;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    @Resource
    IUploadService uploadService;

    @Resource
    ICompetitionService CompetitionService;

    @Override
    public boolean add(Competition competition) throws IOException {
        String filePath = uploadService.upload(competition.getFileImage());
        competition.setImage(filePath);
        competition.setActive(1);
        competition.setCreateTime(LocalDateTime.now());
        return save(competition);
    }

    @Override
    public Object list(Competition competition) {
        // 如果 有name传过来 就按照name 模糊查询
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();

        if(competition.getWithPage() == 1){
            if(StringUtils.isNotBlank(competition.getName())){
                wrapper.like("name",competition.getName());
            }
            if(StringUtils.isNotBlank(competition.getTeacherName())){
                wrapper.eq("t.`realname`",competition.getTeacherName());
            }
            return getBaseMapper().list(new Page<>(competition.getPageNo(),competition.getPageSize()), wrapper);
        } else {
            if(StringUtils.isNotBlank(competition.getName())){
                wrapper.like("name",competition.getName());
            }
            if(StringUtils.isNotBlank(competition.getTeacherName())){
                wrapper.eq("t.`realname`",competition.getTeacherName());
            }
            return getBaseMapper().noPage(wrapper);
        }

    }

    @Override
    public Object getById(Integer id) {
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();
        wrapper.eq("i.`id`",id);
        return getBaseMapper().myGetOne(wrapper);
    }

    @Override
    public boolean batchdel(Integer[] ids) {
        List<Integer> list = new ArrayList<>(ids.length);
        Collections.addAll(list,ids);
        Competition competition = new Competition();
        competition.setActive(0);
        UpdateWrapper<Competition> wrapper = new UpdateWrapper<>();
        wrapper.in("id",list);
        return this.update(competition,wrapper);
    }

    @Override
    public List<Competition> Competitions() {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("active",1);
        List<Competition> queryComp = CompetitionService.list(wrapper);
        return queryComp;
    }
}
