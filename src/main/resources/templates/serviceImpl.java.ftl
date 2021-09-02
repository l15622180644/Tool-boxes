package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 *
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public Page<${entity}> selectPage(BaseParam baseParam){
        Page<${entity}> page = lambdaQuery().page(baseParam.getPAGE(entityClass));
        return page;
    }

    @Override
    public ${entity} selectOne(Long id){
        ${entity} ${entity?uncap_first} = getById(id);
        return ${entity?uncap_first};
    }

    @Override
    public boolean add(${entity} ${entity?uncap_first}){
        return save(${entity?uncap_first});
    }

    @Override
    public boolean modify(${entity} ${entity?uncap_first}){
        return updateById(${entity?uncap_first});
    }

    @Override
    public boolean del(Long id){
        return removeById(id);
    }

    @Override
    public boolean bathDel(List ids){
        return removeByIds(ids);
    }

}
</#if>
