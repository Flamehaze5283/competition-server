package ysu.edu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import ysu.edu.pojo.Competition;
import ysu.edu.service.ICompetitionService;
import ysu.edu.util.ServerResponse;

import javax.annotation.Resource;

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
    ServerResponse SaveCompetition(Competition competition) {
        boolean result = competitionService.add(competition);
        if(result)
            return ServerResponse.success(null, "保存成功");
        else
            return ServerResponse.failed("保存失败");
    }
}
