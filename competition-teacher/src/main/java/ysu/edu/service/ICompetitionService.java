package ysu.edu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ysu.edu.pojo.Competition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-21
 */
public interface ICompetitionService extends IService<Competition> {
    boolean add(Competition competition) throws IOException;
    Object list(Competition competition);
    Object getById(Integer id);
    boolean batchdel(Integer[] ids);

    Competition getByName(String name);

    boolean SaveSign(String parts, Integer id, List<String> activeParams) throws JsonProcessingException;
}
