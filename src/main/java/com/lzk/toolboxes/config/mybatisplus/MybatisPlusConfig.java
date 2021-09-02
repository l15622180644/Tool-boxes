package com.lzk.toolboxes.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @module
 * @date 2021/6/19 11:17
 */
@Configuration
@MapperScan("")
public class MybatisPlusConfig {

    /**
     * 分页插件
     * 乐观锁插件
     * 在实体类的字段上加上@Version注解
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//添加分页插件
//        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//添加乐观锁插件
//        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());//针对 update 和 delete 语句 作用: 阻止恶意的全表更新删除
        return interceptor;
    }


}
