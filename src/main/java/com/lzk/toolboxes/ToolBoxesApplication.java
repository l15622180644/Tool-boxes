package com.lzk.toolboxes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})//取消springboot对freemarker的自动配置，使用自定义配置
public class ToolBoxesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolBoxesApplication.class, args);
        System.out.println("服务启动成功");
    }

}
