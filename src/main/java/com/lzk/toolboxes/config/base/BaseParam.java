package com.lzk.toolboxes.config.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author
 * @module
 * @date 2021/6/19 12:05
 */
@Data
public class BaseParam implements Serializable {

    private Integer page;

    private Integer limit;

    public <T> Page<T> getPage() {
        return new Page<T>(page != null ? page : 1, limit != null ? limit : 10);
    }

}
