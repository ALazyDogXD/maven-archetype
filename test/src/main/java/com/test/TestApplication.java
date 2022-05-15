package com.test;

import com.test.test.TestImportSelector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 13:48
 * @description
 */

@MapperScan("com.test.mapper")
@SpringBootApplication
@Import(TestImportSelector.class)
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
