package ${package.Controller};


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ${package.Entity}.${entity};
<#--<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>-->
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 *
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    /**
     * 查询【】列表
     */
    @PostMapping("/get${entity}Page")
    public BaseResult getPage(@RequestBody BaseParam baseParam){

    }

    /**
     * 获取【】详情
     */
    @PostMapping("/get${entity}One")
    public BaseResult getOne(@RequestBody BaseParam baseParam){

    }

    /**
     * 新增【】
     */
    @PostMapping("/add${entity}")
    @Transactional
    public BaseResult add(@RequestBody ${entity} ${entity?uncap_first}){

    }

    /**
     * 修改【】
     */
    @PostMapping("/update${entity}")
    @Transactional
    public BaseResult update(@RequestBody ${entity} ${entity?uncap_first}){

    }

    /**
     * 删除【】
     */
    @PostMapping("/del${entity}")
    @Transactional
    public BaseResult del(@RequestBody BaseParam baseParam){

    }

    /**
     * 批量删除【】
     */
    @PostMapping("/bathDel${entity}")
    @Transactional
    public BaseResult bathDel(@RequestBody BaseParam baseParam){

    }


}
</#if>
