package ysu.edu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stu-test")
public class TestController {

    @GetMapping
    String test() {
        return "test success";
    }
}
