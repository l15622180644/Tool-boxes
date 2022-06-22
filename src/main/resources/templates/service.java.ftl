package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseParam;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseResult;

/**
 *
 * @author
 * @since ${date}
 */

<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    BaseResult get${entity}Page(BaseParam param);

    BaseResult<${entity}> get${entity}One(BaseParam param);

    BaseResult<Boolean> add${entity}(${entity} ${entity?uncap_first});

    BaseResult<Boolean> update${entity}(${entity} ${entity?uncap_first});

    BaseResult<Boolean> del${entity}(BaseParam param);

    BaseResult<Boolean> bathDel${entity}(BaseParam param);

}
</#if>
