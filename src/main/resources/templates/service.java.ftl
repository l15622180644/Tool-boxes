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

    BaseResult get${entity}One(BaseParam param);

    BaseResult add${entity}(${entity} ${entity?uncap_first});

    BaseResult update${entity}(${entity} ${entity?uncap_first});

    BaseResult del${entity}(BaseParam param);

    BaseResult bathDel${entity}(BaseParam param);

}
</#if>
