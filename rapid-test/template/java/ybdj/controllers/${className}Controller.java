<#assign className = table.className>
<#assign underscoreName = table.underscoreName>
<#assign idColumn = table.idColumn>
<#assign classNameFirstLower = table.classNameFirstLower>
<#assign classNameLower = className?uncap_first>
<#assign service = table.classNameFirstLower+"Service">
package ${basepackage}.controllers;

import com.sqbj.ybdj.api.utils.ListHelper;
import org.springframework.web.bind.annotation.*;
import com.sqbj.ybdj.api.core.annotation.SessionRequired;
import org.springframework.beans.factory.annotation.Autowired;
import com.sqbj.ybdj.api.core.annotation.SessionRequired;
import com.sqbj.ybdj.dependency.web.api.core.controllers.AbstractBaseController;
import com.sqbj.ybdj.dependency.web.api.core.utils.AssertUtil;
import com.sqbj.ybdj.dependency.web.api.core.utils.BeanMapping;
import com.sqbj.ybdj.dependency.web.api.core.bean.response.DataResponse;
import com.sqbj.ybdj.dependency.web.api.core.bean.response.PageResponse;

import com.sqbj.ybdj.api.utils.DateNewUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/**
<#include "/include/common/java_description.include">
 */
@RestController
@RequestMapping("/${underscoreName?replace('_','/')}")
public class ${className}Controller extends AbstractBaseController{

    @Autowired
    private ${className}Service ${service};

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SessionRequired
    public DataResponse<${className}Response> add(@RequestBody ${className}AddRequest request) {

        ${className}Entity entity = ${service}.add(request);
        ${className}Response response = BeanMapping.map(entity, ${className}Response.class);
        return new DataResponse<>(response);
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @SessionRequired
    public DataResponse<${className}Response> add(@RequestBody ${className}ModifyRequest request) {
        AssertUtil.notNull(request.getId(), "Error", "id为空");

        ${className}Entity entity = ${service}.modify(request);
        ${className}Response response = BeanMapping.map(entity, ${className}Response.class);
        return new DataResponse<>(response);
    }

    /**
     * 根据id获取详细
     *
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @SessionRequired
    public DataResponse<${className}Response> findOne(@PathVariable Long id) {
        AssertUtil.notNull(id, "Error", "id为空");
        ${className}Entity entity = ${service}.findOneById(id);
        ${className}Response response = BeanMapping.map(entity, ${className}Response.class);
        return new DataResponse<>(response);
    }


    /**
     * 根据ids获取详细
     *
     * @return
     */
    @RequestMapping(value = "/get/infos", method = RequestMethod.GET)
    @SessionRequired
    public DataResponse<List<${className}Response>> batchGet(@RequestParam String ids) {
        AssertUtil.isNotEmpty(ids, "Error", "id为空");
        List<Integer> idsList = ListHelper.convertArrayToIntList(ids);
        AssertUtil.notNull(idsList, "Error", "ids为空");
        AssertUtil.assertFalse(idsList.isEmpty(), "Error", "ids为空");
        List<${className}Entity> entities = ${service}.findListByIds(idsList);

        List<${className}Response> response = BeanMapping.mapList(entities, ${className}Response.class);
        return new DataResponse<>(response);
    }

    /**
     * 根据id删除信息
     *
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @SessionRequired
    public DataResponse<Object> delete(@PathVariable Long id) {
        AssertUtil.notNull(id, "Error", "id为空");

        ${service}.deleteOneById(id);
        return new DataResponse<>("success");
    }

    /**
     * 根据ids删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @SessionRequired
    public DataResponse<Object> deleteBatch(@RequestParam String ids) {
        AssertUtil.isNotEmpty(ids, "Error", "id为空");
        List<Integer> idsList = ListHelper.convertArrayToIntList(ids);
        AssertUtil.notNull(idsList, "Error", "ids为空");
        AssertUtil.assertFalse(idsList.isEmpty(), "Error", "ids为空");

        ${service}.deleteByIds(ids);
        return new DataResponse<>("success");
    }


    /**
     * 根据id标记删除信息
     *
     * @return
     */
    @RequestMapping(value = "/flag/{id}", method = RequestMethod.DELETE)
    @SessionRequired
    public DataResponse<Object> flagDelete(@PathVariable Long id) {
        AssertUtil.notNull(id, "Error", "id为空");

        ${service}.flagDeleteOneById(id);
        return new DataResponse<>("success");
    }

    /**
     * 根据ids标记删除信息
     *
     * @return
     */
    @RequestMapping(value = "/flag/deleteBatch", method = RequestMethod.DELETE)
    @SessionRequired
    public DataResponse<Object> flagDeleteBatch(@RequestParam String ids) {
        AssertUtil.isNotEmpty(ids, "Error", "id为空");
        List<Integer> idsList = ListHelper.convertArrayToIntList(ids);
        AssertUtil.notNull(idsList, "Error", "ids为空");
        AssertUtil.assertFalse(idsList.isEmpty(), "Error", "ids为空");

        ${service}.flagDeleteBatchByIds(ids);
        return new DataResponse<>("success");
    }


    /**
     * 分页查询
     */
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    @SessionRequired
    public PageResponse<${className}Response> pageList(@RequestParam(required = false, defaultValue = "0") Integer pageIndex,
                                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                    <#list table.columns as column>
                                                        <#if column.columnNameLower == idColumn>
                                                        <#elseif column.isDateTimeColumn>
                                                            @RequestParam(required = false)  String ${column.columnNameLower}Start,
                                                            @RequestParam(required = false)  String ${column.columnNameLower}End,
                                                        <#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
                                                        <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
                                                            @RequestParam(required = false)  ${enumName} ${column.columnNameLower},
                                                        <#else>
                                                            <#if column.javaType[0..8]=='java.lang'>
                                                                <#if column.columnNameLower?lower_case?index_of("id")!=-1>
                                                            @RequestParam(required = false)  String ${column.columnNameLower}s,
                                                                <#else>
                                                            @RequestParam(required = false)  ${column.javaType?substring(10)} ${column.columnNameLower},
                                                                </#if>
                                                            <#else>
                                                            @RequestParam(required = false)  ${column.javaType} ${column.columnNameLower},
                                                            </#if>
                                                        </#if>
                                                        </#list>
                                                            @RequestParam(required = false)  String orderBys) {

        ${className}PageRequest request = new ${className}PageRequest();
<#list table.columns as column>
    <#if column.columnNameLower == idColumn>
    <#elseif column.isDateTimeColumn>
        //设置查询时间
        DateNewUtil.DateSearch ${column.columnNameLower}Search = DateNewUtil.verifyDate(${column.columnNameLower}Start, ${column.columnNameLower}End);
        request.set${column.columnName}Start(${column.columnNameLower}Search.getStart());
        request.set${column.columnName}End(${column.columnNameLower}Search.getEnd());

    <#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1) >
        <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
        request.set${column.columnName}(${column.columnNameLower});
    <#else>
        <#if column.columnNameLower?lower_case?index_of("id")!=-1>
        request.set${column.columnName}s(ListHelper.convertArrayToIntList(${column.columnNameLower}s));
        <#else>
        request.set${column.columnName}(${column.columnNameLower});
        </#if>
    </#if>
</#list>
        List<Sort.Order> orders = new ArrayList<>();

        Pageable pageable = new PageRequest(pageIndex, pageSize, new Sort(orders));

        Page<${className}Entity> pageList = ${service}.pageList(request, pageable);

        List<${className}Response> list = new ArrayList<>();
        pageList.getContent().forEach(o -> {
            ${className}Response response = BeanMapping.map(o, ${className}Response.class);

            list.add(response);
        });
        return new PageResponse<>(pageIndex, pageSize, pageList.getTotalElements(), list);
    }
}