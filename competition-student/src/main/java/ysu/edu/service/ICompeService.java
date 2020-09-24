package ysu.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import ysu.edu.util.ServerResponse;
@FeignClient("teacher")
public interface ICompeService {
        @RequestMapping("/competition/list")
        ServerResponse list();

}
