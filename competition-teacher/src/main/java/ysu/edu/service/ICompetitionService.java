package ysu.edu.service;

import org.springframework.web.multipart.MultipartFile;
import ysu.edu.pojo.Competition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

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
}
