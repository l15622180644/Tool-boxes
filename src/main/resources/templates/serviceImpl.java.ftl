package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseParam;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseResult;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.status.Status;

/**
 *
 * @author
 * @since ${date}
 */

@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public BaseResult get${entity}Page(BaseParam param){
        Page<${entity}> page = lambdaQuery()
            .orderBy(true,param.getIsASCByCreateTime()!=null?param.getIsASCByCreateTime():false, ${entity}::getCreateTime)
            .page(param.getPage(entityClass));
        return BaseResult.returnResult(page);
    }

    @Override
    public BaseResult get${entity}One(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(getById(param.getId()));
    }

    @Override
    public BaseResult add${entity}(${entity} ${entity?uncap_first}){
        return BaseResult.returnResult(save(${entity?uncap_first}),${entity?uncap_first}.getId());
    }

    @Override
    public BaseResult update${entity}(${entity} ${entity?uncap_first}){
        if (${entity?uncap_first}.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(updateById(${entity?uncap_first}));
    }

    @Override
    public BaseResult del${entity}(BaseParam param){
        if (param.getId() == null) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeById(param.getId()));
    }

    @Override
    public BaseResult bathDel${entity}(BaseParam param){
        if (param.getIds() == null || param.getIds().isEmpty()) return BaseResult.error(Status.PARAMEXCEPTION);
        return BaseResult.returnResult(removeByIds(param.getIds()));
    }

}
</#if>
