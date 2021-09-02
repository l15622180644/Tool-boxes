package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.lzk.codegenerator.common.base.BaseParam;

/**
 *
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    Page<${entity}> selectPage(BaseParam baseParam);

    ${entity} selectOne(Long id);

    boolean add(${entity} ${entity?uncap_first});

    boolean modify(${entity} ${entity?uncap_first});

    boolean del(Long id);

    boolean bathDel(List ids);

}
</#if>
