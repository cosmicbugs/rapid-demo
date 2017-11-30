<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign hasStatusColumn = false>
<#list table.columns as column>
<#if column.columnNameLower?lower_case?index_of("status")!=-1>
<#assign hasStatusColumn = true>
</#if>
</#list>
package ${basepackage}.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ${basepackage}.entity.${className}Entity;
import ${basepackage}.bean.request.${className}AddRequest;
import ${basepackage}.bean.request.${className}ModifyRequest;
import ${basepackage}.bean.request.${className}PageRequest;
/**
<#include "/include/common/java_description.include">
 */
public interface ${className}Service {

    /**
     * 新增
     */
    ${className}Entity add(${className}AddRequest request);

    /**
     * 更新
     */
    ${className}Entity modify(${className}ModifyRequest request);

    /**
     * 根据id获取对象
     */
    ${className}Entity findOneById(Integer id);

    /**
     * 根据ids查询对象
     */
    List<${className}Entity> findListByIds(List<Integer> ids);

    /**
     * 根据id物理删除对象
     */
    void deleteOneById(Integer id);

    /**
     * 根据ids物理删除对象
     */
    void deleteByIds(List<Integer> ids);

<#if hasStatusColumn>
    /**
     * 根据id标记删除信息
     */
    void flagDeleteOneById(Integer id);

    /**
     * 根据ids标记删除信息
     */
    void flagDeleteBatchByIds(List<Integer> ids);

</#if>
    /**
     * 分页查询
     */
    Page<${className}Entity> pageList(${className}PageRequest request, Pageable pageable);


}