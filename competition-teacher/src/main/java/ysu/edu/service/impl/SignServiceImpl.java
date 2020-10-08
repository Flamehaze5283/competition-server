package ysu.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ysu.edu.pojo.Competition;
import ysu.edu.pojo.Sign;
import ysu.edu.mapper.SignMapper;
import ysu.edu.pojo.Student;
import ysu.edu.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import ysu.edu.util.TeamDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return getBaseMapper().getList(wrapper);
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

    @Override
    public boolean submit (String str,String active) {
        //System.out.println("IMpl:" + str);
        JSONObject obj = JSONObject.parseObject(str);
        JSONArray actives = JSONArray.parseArray(active);
        if (obj != null) {
            Sign sign = new Sign();
            //id
            if(obj.get("Id") != null){
                sign.setId(Integer.parseInt(obj.get("Id").toString()));
            }
            //competitionId
            if(obj.get("competitionId") != null){
                sign.setCompetitionId(Integer.parseInt(obj.get("competitionId").toString()));
            }
            StringBuilder stuIds = new StringBuilder();
            //captainId
            if(obj.get("captainId") != null){
                sign.setCaptainId(obj.get("captainId").toString());

                stuIds.append(obj.get("captainId").toString());
                if(obj.get("studentId") != null) {  //队长后面有队员再加逗号
                    stuIds.append(",");
                }
            }else {
                sign.setCaptainId("0");  //没有队长就设置为0
            }
            //studentId,studentName

            if(obj.get("studentId") != null){
                JSONArray jsonArray1 = obj.getJSONArray("studentId");
                for(int i = 0 ; i  < jsonArray1.size() ; i++){
                    stuIds.append(jsonArray1.get(i));
                    if( i != jsonArray1.size()-1){
                        stuIds.append(",");
                    }
                }
                sign.setStudentId(stuIds.toString());
            }

            //teamName
            if(obj.get("teamName") != null){
                sign.setTeamName(obj.get("teamName").toString());
            }
            //活动字段的添加
            //StringBuilder strActive = new StringBuilder();
            JSONObject jsonObject =new JSONObject();
            for (int i = 0; i < actives.size(); i++) {
                //System.out.println("this is active:" + actives.get(i));
                if( obj.get(actives.get(i).toString()) != null){
                    jsonObject.put(actives.get(i).toString(),obj.get(actives.get(i).toString()));

                   /* strActive.append(actives.get(i).toString());
                    strActive.append(":");
                    strActive.append(obj.get(actives.get(i).toString()).toString());
                    if(i != actives.size()-1){
                        strActive.append(",");
                    }*/
                }
            }
            sign.setOptionItem(jsonObject.toString());
            sign.setVerify(0);
            sign.setActive(1);
            LocalDateTime local = LocalDateTime.now();
            sign.setCreateTime(local);
            return this.saveOrUpdate(sign);
        }
        return false;
    }

    @Override
    public Object detailSelfList (Sign sign) {
        QueryWrapper<Sign> wrapper = new QueryWrapper<>();
        if(sign.getId() != null)
            wrapper.eq("sign.id", sign.getId());
        if(sign.getStudentId() != null)
            wrapper.like("concat(',',student_id,',')", "," + sign.getStudentId() + ",");
        if(StringUtils.isNotBlank(sign.getCompName()))
            wrapper.like("comp.name", sign.getCompName());
        List<Sign> comb = combineStudents(getBaseMapper().selfList(wrapper));
        String []stuid = comb.get(0).getStudentId().split(",");

        StringBuilder stuname = new StringBuilder();
        for(int i=0;i<stuid.length;i++){
            QueryWrapper<Student> wrapp = new QueryWrapper<>();
            wrapp.eq("num_id",stuid[i]);
            stuname.append(getBaseMapper().stuName(wrapp).getName());
            if(i!= stuid.length-1){
                stuname.append(",");
            }
        }
        System.out.println("stunames:"+stuname);
        comb.get(0).setStudentName(stuname.toString());
        return comb;
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
