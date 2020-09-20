package ysu.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ysu.edu.mapper")
public class StudentApp {
    public static void main(String[] args) {
        SpringApplication.run(StudentApp.class);
    }
}
