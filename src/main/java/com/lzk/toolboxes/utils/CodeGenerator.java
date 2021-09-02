package com.lzk.toolboxes.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @module
 * @date 2021/5/24 8:53
 */
public class CodeGenerator {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final String PARENT_PACKAGE = "com.lzk.codegenerator";  //项目包名
    private final String PARENT_PATH = "com/lzk/codegenerator";
    private final String OUTPUT_PATH = "E:/lzk/代码生成"; //落地路径
    private final String PROJECT_PATH = "/gen";
    private final String ROOT_PATH1 = PROJECT_PATH + "/src/main/java/";
    private final String ROOT_PATH2 = PROJECT_PATH + "/src/main/resources/";
    //    模板路径
    private final String ENTITY_TEMPLATE = "/templates/entity.java.ftl";
    private final String MAPPER_TEMPLATE = "/templates/mapper.java.ftl";
    private final String XML_TEMPLATE = "/templates/mapper.xml.ftl";
    private final String SERVICE_TEMPLATE = "/templates/service.java.ftl";
    private final String IMPL_TEMPLATE = "/templates/serviceImpl.java.ftl";
    private final String CONTROLLER_TEMPLATE = "/templates/controller.java.ftl";
    //    文件输出路径
    private final String ENTITY_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH1 + PARENT_PATH + "/entity/";
    private final String MAPPER_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH1 + PARENT_PATH + "/mapper/";
    private final String XML_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH2 + "/mapper/";
    private final String SERVICE_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH1 + PARENT_PATH + "/service/";
    private final String IMPL_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH1 + PARENT_PATH + "/service/impl/";
    private final String CONTROLLER_OUTPUT_PATH = OUTPUT_PATH + ROOT_PATH1 + PARENT_PATH + "/controller/";
    //    zip文件名
    private final String ZIP_NAME = "gen.zip";
    //    数据源
    private final String url = "jdbc:mysql://localhost:3306/test?useSSL=false&Unicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&allowPublicKeyRetrieval=true";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String userName = "root";
    private final String password = "123456";


    public void genCode(String tableName, boolean isZip, boolean isDown) throws Exception {
//        生成器
        AutoGenerator generator = new AutoGenerator();
//        设置模板引擎
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
//        全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(OUTPUT_PATH); //配置文件输出路径
        globalConfig.setAuthor("lzk"); //作者
        globalConfig.setOpen(false); //是否打开文件
        globalConfig.setServiceName("%sService"); //配置逻辑层接口名字,不配置默认会有前缀’I’
        globalConfig.setIdType(IdType.ASSIGN_ID); //配置主键类型
        generator.setGlobalConfig(globalConfig);
//        数据源配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDbType(DbType.MYSQL)
                .setUrl(url)
                .setDriverName(driver)
                .setUsername(userName)
                .setPassword(password);
        generator.setDataSource(dataSource);
//        策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tableName) //需要生成的表名
                .setNaming(NamingStrategy.underline_to_camel) //实体命名方式,根据表名下划线驼峰命名
                .setColumnNaming(NamingStrategy.underline_to_camel) //实体属性命名方式
                .setEntityLombokModel(true) //实体使用lombok
                .setRestControllerStyle(true); //控制器使用restController注解

        generator.setStrategy(strategy);
//        包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PARENT_PACKAGE);
        generator.setPackageInfo(packageConfig);
//        模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(null)
                .setMapper(null)
                .setXml(null)
                .setService(null)
                .setServiceImpl(null)
                .setController(null);
        generator.setTemplate(templateConfig);
//        自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };
        injectionConfig.setFileOutConfigList(fileOutConfigList());
        generator.setCfg(injectionConfig);
//        生成
        log.info("开始生成【" + tableName + "】");
        generator.execute();
        if (isExist(OUTPUT_PATH, ROOT_PATH1) && isZip) {
            log.info("开始压缩...");
            FileZip.zip(OUTPUT_PATH + PROJECT_PATH, OUTPUT_PATH + "/" + ZIP_NAME);
            log.info("压缩完成");
            if (isDown) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(OUTPUT_PATH + "/" + ZIP_NAME));
                byte[] bytes = new byte[1024];
                HttpServletResponse response = ServletUtil.getResponse();
                response.setContentType("application/x-msdownload");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ZIP_NAME, "UTF-8"));
                ServletOutputStream out = response.getOutputStream();
                int len = 0;
                while ((len = inputStream.read(bytes)) > 0)
                    out.write(bytes, 0, len);
                out.close();
                inputStream.close();
                delFile(new File(OUTPUT_PATH + PROJECT_PATH));
                delFile(new File(OUTPUT_PATH + "/" + ZIP_NAME));
            }
        }
    }


    //        自定义文件输出
    private List<FileOutConfig> fileOutConfigList() {
        List<FileOutConfig> list = new ArrayList<>();
//        entity文件输出
        list.add(new FileOutConfig(ENTITY_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return ENTITY_OUTPUT_PATH + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
//        mapper文件输出
        list.add(new FileOutConfig(MAPPER_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return MAPPER_OUTPUT_PATH + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });
//        xml文件输出
        list.add(new FileOutConfig(XML_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return XML_OUTPUT_PATH + tableInfo.getXmlName() + StringPool.DOT_XML;
            }
        });
//        service文件输出
        list.add(new FileOutConfig(SERVICE_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return SERVICE_OUTPUT_PATH + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });
//        impl文件输出
        list.add(new FileOutConfig(IMPL_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return IMPL_OUTPUT_PATH + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
//        controller文件输出
        list.add(new FileOutConfig(CONTROLLER_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return CONTROLLER_OUTPUT_PATH + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });
        return list;
    }

    public boolean isExist(String parentPath, String childPath) {
        File file = new File(parentPath, childPath);
        return file.exists();
    }

    public void delFile(File file) {
        File[] files = new File[0];
        if (file.isDirectory()) {
            files = file.listFiles();
        }
        for (File file1 : files) {
            if (file1.isDirectory()) {
                delFile(file1);
            } else {
                file1.delete();
            }
        }
        file.delete();
    }


}
