package ysu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ysu.edu.pojo.Sign;
import ysu.edu.mapper.SignMapper;
import ysu.edu.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.util.TeamDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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

    @Override
    public Object getList(Sign sign) {
        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id",sign.getCompetitionId());
        return getBaseMapper().list(wrapper);
    }

    @Override
    public Object selfList(Sign sign) {
        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        if(sign.getCompType() != null)
            wrapper.eq("comp.type", sign.getCompType());
        if(sign.getId() != null)
            wrapper.eq("sign.id", sign.getId());
        if(sign.getStudentId() != null)
            wrapper.like("concat(',',student_id,',')", "," + sign.getStudentId() + ",");
        if(StringUtils.isNotBlank(sign.getCompName()))
            wrapper.like("comp.name", sign.getCompName());
        return combineStudents(getBaseMapper().selfList(wrapper));
    }

    private List<Sign> combineStudents(List<Sign> list) {
        HashMap<Integer, StringBuffer> map = new HashMap<>();
        for(int i = 0; i < list.size(); i++) {
            Sign now = list.get(i);
            Integer id = now.getId();
            String name = now.getStudentName();
            if(map.get(id) == null) {
                map.put(id, new StringBuffer(name));
            } else {
                map.put(id, map.get(id).append(',').append(name));
                list.remove(i--);
            }
        }
        for(Sign now : list)
            now.setStudentName(map.get(now.getId()).toString());
        return list;
    }

    @Override
    public TeamDTO overList(Sign sign, String[] students) {

        TeamDTO dto = new TeamDTO();

        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id",sign.getCompetitionId());
        List<Sign> teamArrayList= getBaseMapper().theTeamList(wrapper);

        QueryWrapper<Sign> wrapper2 = new QueryWrapper<>();
        wrapper2.in("num_id",students);
        List<Sign> personArrayList = getBaseMapper().theTeam2List(wrapper2);

        dto.setTeamArrayList(teamArrayList);
        dto.setPersonArrayList(personArrayList);
        return dto;
    }

    @Override
    public Object teamList(Sign sign){
        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id",sign.getCompetitionId());
        return getBaseMapper().theTeamList(wrapper);
    }
}
