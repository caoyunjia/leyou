package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.leyou.item.mapper")
@EnableSwagger2
public class LyItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class, args);
    }
}
