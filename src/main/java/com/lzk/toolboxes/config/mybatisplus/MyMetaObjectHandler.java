package com.lzk.toolboxes.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lzk.toolboxes.utils.TimeHelper;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author
 * @module
 * @date 2021/6/19 11:02
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "createTime", TimeHelper::getCurrentTime10, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", TimeHelper::getCurrentTime10, Long.class);
    }
}
