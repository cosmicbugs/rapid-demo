<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign underscoreName = table.underscoreName>
<#assign hasStatusColumn = false>
<#list table.columns as column>
<#if column.columnNameLower?lower_case?index_of("status")!=-1>
<#assign hasStatusColumn = true>
</#if>
</#list>
package ${basepackage}.dao;

import ${project_package_prefix}.dependency.web.api.core.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ${basepackage}.entity.${className}Entity;

import java.util.List;
<#include "/include/ybdj_include/enum_imports.include">

/**
<#include "/include/common/java_description.include">
 */
public interface ${className}Dao extends BaseDao<${className}Entity, Integer> {

    /**
     * 根据ids批量查询对象
     * @param ids
     * @return
     */
    List<${className}Entity> findByIdIn(List<Integer> ids);

    /**
     * 根据id物理删除数据
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据ids物理删除数据
     * @param ids
     */
    void deleteByIdIn(List<Integer> ids);
<#if hasStatusColumn>

    /**
     * 根据ids、状态批量查询对象
     * @param ids
     * @param statusNew
     * @return
     */
    List<SysMenuEntity> findByIdInAndStatus(List<Integer> ids, NormalStatusNew statusNew);

</#if>

}