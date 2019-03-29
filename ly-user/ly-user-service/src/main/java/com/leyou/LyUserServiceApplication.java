package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@FeignClient
@MapperScan("com.leyou.user.mapper")
public class LyUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyUserServiceApplication.class, args);
    }

}
