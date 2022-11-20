package com.lzk.toolboxes.config.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * @author
 * @module
 * @date 2022/11/2 15:26
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    int insertBatchSomeColumn(Collection<T> entityList);
}
