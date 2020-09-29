package ysu.edu.controller;


import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.Competition;
import ysu.edu.service.ICompetitionService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zh
 * @since 2020-09-20
 */
@RestController
@RequestMapping("/competition")
public class CompetitionController {
    @Resource
    ICompetitionService competitionService;

    @PostMapping("/save-competition")
    ServerResponse SaveCompetition(Competition competition) throws IOException {
        boolean result = competitionService.add(competition);
        if(result)
            return ServerResponse.success(null, "保存成功");
        else
            return ServerResponse.failed("保存失败");
    }

    @GetMapping("/getByName")
    ServerResponse GetCompetition(String name) {
        Competition competition = competitionService.getByName(name);
        if(ObjectUtils.isEmpty(competition))
            return ServerResponse.failed("未找到该竞赛相关信息");
        else
            return ServerResponse.success(competition);
    }

    @GetMapping("/list")
    ServerResponse list(Competition competition) {
        return ServerResponse.success(competitionService.list(competition));
    }
    @GetMapping("/getById")
    ServerResponse getById(Integer id){
        return ServerResponse.success(competitionService.getById(id));
    }
    @PostMapping("/del")
    ServerResponse del(Competition competition) {
        competition.setActive(0);
        return ServerResponse.success(competitionService.updateById(competition));
    }
    @PostMapping("/batchdel")
    ServerResponse batchdel(Integer[] ids) {
        return ServerResponse.success(competitionService.batchdel(ids));
    }
    @PostMapping("/back")
    ServerResponse back(Competition competition) {
        competition.setActive(1);
        return ServerResponse.success(competitionService.updateById(competition));
    }
}
