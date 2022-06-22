package com.lzk.toolboxes.config.freemarker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author
 * @module
 * @date 2022/4/24 17:19
 */

@Configuration
public class FreemarkerConfig {

    @Value("${freemarker.template-load-path}")
    private String LOAD_PATH;

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration(){
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        configuration.unsetCacheStorage();
        try{
            configuration.setDirectoryForTemplateLoading(new File(LOAD_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        return configuration;
    }


}
