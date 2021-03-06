package ${package.Controller};


import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import ${package.Entity}.${entity};
<#--<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>-->
import javax.annotation.Resource;
import java.util.List;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Service}.${table.serviceName};
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseParam;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.base.BaseResult;
import ${package.Entity?substring(0,(package.Entity?last_index_of(".")))}.common.annotation.Log;

/**
 *
 *
 * @author
 * @since ${date}
 */

<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>Service")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {

</#if>
    @Resource
    private ${table.serviceName} ${table.serviceName?uncap_first};

    /**
     * 查询列表
     */
    @PostMapping("/get${entity}Page")
    public BaseResult<List<${entity}>> getPage(@RequestBody BaseParam param){
        return ${table.serviceName?uncap_first}.get${entity}Page(param);
    }

    /**
     * 获取详情
     */
    @PostMapping("/get${entity}One")
    public BaseResult<${entity}> getOne(@RequestBody BaseParam param){
        return ${table.serviceName?uncap_first}.get${entity}One(param);
    }

    /**
     * 新增
     */
    @PostMapping("/add${entity}")
    @Transactional
    @Log(module = "",content = "新增",type = Log.INSERT)
    public BaseResult<Boolean> add(@RequestBody ${entity} ${entity?uncap_first}){
        return ${table.serviceName?uncap_first}.add${entity}(${entity?uncap_first});
    }

    /**
     * 修改
     */
    @PostMapping("/update${entity}")
    @Transactional
    @Log(module = "",content = "修改",type = Log.UPDATE)
    public BaseResult<Boolean> update(@RequestBody ${entity} ${entity?uncap_first}){
        return ${table.serviceName?uncap_first}.update${entity}(${entity?uncap_first});
    }

    /**
     * 删除
     */
    @PostMapping("/del${entity}")
    @Transactional
    @Log(module = "",content = "删除",type = Log.DELETE)
    public BaseResult<Boolean> del(@RequestBody BaseParam param){
        return ${table.serviceName?uncap_first}.del${entity}(param);
    }

    /**
     * 批量删除
     */
    @PostMapping("/bathDel${entity}")
    @Transactional
    @Log(module = "",content = "批量删除",type = Log.DELETE)
    public BaseResult<Boolean> bathDel(@RequestBody BaseParam param){
        return ${table.serviceName?uncap_first}.bathDel${entity}(param);
    }


}
</#if>
