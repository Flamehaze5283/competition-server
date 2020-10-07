package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import ysu.edu.dto.SignFormDTO;
import ysu.edu.pojo.Competition;
import ysu.edu.mapper.CompetitionMapper;
import ysu.edu.service.ICompetitionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import ysu.edu.service.IImageUploadService;

import javax.annotation.Resource;
import java.util.List;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
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
    IImageUploadService uploadService;

    @Resource
    ICompetitionService CompetitionService;

    @Override
    public boolean add(Competition competition) throws IOException {
        String name = competition.getName();
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        if(ObjectUtils.isNotEmpty(this.getOne(wrapper)))
            return false;
        else {
//            String filePath = "";
//            if(ObjectUtils.isNotEmpty(competition.getFileImage()))
            String filePath = uploadService.uploadImage(competition.getFile()).getData().toString();
            competition.setImage(filePath);
            competition.setActive(1);
            competition.setCreateTime(LocalDateTime.now());
            return this.save(competition);
        }
    }

    @Override
    public Object list(Competition competition) {
        // 如果 有name传过来 就按照name 模糊查询
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();
        if(competition.getWithPage() == 1){
            //筛选了竞赛级别和类型
            if(StringUtils.isNotBlank(competition.getLevelName()) && StringUtils.isNotBlank(competition.getTypeName())){
                if(StringUtils.isNotBlank(competition.getName())){
                    wrapper.eq("l.name",competition.getLevelName()).eq("r.name",competition.getTypeName()).like("i.name",competition.getName());
                }else{
                    wrapper.eq("l.name",competition.getLevelName()).eq("r.name",competition.getTypeName());
                }
                return getBaseMapper().myGetPage(new Page<>(competition.getPageNo(),competition.getPageSize()), wrapper);
            }
            //筛选了竞赛级别
            if(StringUtils.isNotBlank(competition.getLevelName())){
                if(StringUtils.isNotBlank(competition.getName())){
                    wrapper.eq("l.name",competition.getLevelName()).like("i.name",competition.getName());
                }else{
                    wrapper.eq("l.name",competition.getLevelName());
                }
                return getBaseMapper().myGetPage(new Page<>(competition.getPageNo(),competition.getPageSize()), wrapper);
            }
            //筛选了竞赛类型
            if(StringUtils.isNotBlank(competition.getTypeName())){
                if(StringUtils.isNotBlank(competition.getName())){
                    wrapper.eq("r.name",competition.getTypeName()).like("i.name",competition.getName());
                }else{
                    wrapper.eq("r.name",competition.getTypeName());
                }
                return getBaseMapper().myGetPage(new Page<>(competition.getPageNo(),competition.getPageSize()), wrapper);
            }
            //直接查找比赛名称，指导教师名字
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
    public Competition getByName(String name) {
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        return this.getOne(wrapper);
    }

    @Override
    public boolean SaveSign(String parts, Integer id, List<String> activeParams) throws JsonProcessingException {
        SignFormDTO formDTO = new SignFormDTO();
        formDTO.setParts(parts);
        formDTO.setActiveParams(activeParams);
        ObjectMapper mapper = new ObjectMapper();
        String optionList = mapper.writeValueAsString(formDTO);
        Competition competition = new Competition();
        competition.setId(id);
        competition.setOptionList(optionList);
        return this.updateById(competition);
    }

    @Override
    public String getOptionList (Integer id) {
        QueryWrapper<Competition> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        List<Competition> competition = this.list(wrapper);
        return competition.get(0).getOptionList();
    }

}
