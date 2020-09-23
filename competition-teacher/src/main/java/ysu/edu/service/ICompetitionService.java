package ysu.edu.service;

import ysu.edu.pojo.Competition;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zh
 * @since 2020-09-21
 */
public interface ICompetitionService extends IService<Competition> {

    boolean add(Competition competition);
}
