<#assign className = table.className>
<#assign classNameFirstLower = table.classNameFirstLower>
<#assign idColumn = table.idColumn>
<#assign dao = classNameFirstLower+"Dao">
<#assign classNameLower = className?uncap_first>
<#assign hasStatusColumn = false>
<#list table.columns as column>
<#if column.columnNameLower?lower_case?index_of("status")!=-1>
<#assign hasStatusColumn = true>
</#if>
</#list>
package ${basepackage}.service.impl;

import ${project_package_prefix}.dependency.web.api.core.service.AbstractBaseServiceImpl;
import ${project_package_prefix}.dependency.web.api.core.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.criteria.Predicate;

import ${basepackage}.dao.${className}Dao;
import ${basepackage}.entity.${className}Entity;
import ${basepackage}.service.${className}Service;
import ${basepackage}.bean.request.${className}AddRequest;
import ${basepackage}.bean.request.${className}ModifyRequest;
import ${basepackage}.bean.request.${className}PageRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
<#include "/include/ybdj_include/enum_imports.include">

/**
<#include "/include/common/java_description.include">
 */
@Service("${classNameFirstLower}Service")
@Transactional
public class ${className}ServiceImpl extends AbstractBaseServiceImpl implements ${className}Service {

    @Autowired
    private ${className}Dao ${dao};


    /**
     * 新增
     */
    @Override
    public ${className}Entity add(${className}AddRequest request) {
        ${className}Entity entity = new ${className}Entity();
        copyUpdatableField(entity, request);
        ${dao}.save(entity);
        return entity;
    }

    /**
     * 更新
     */
    @Override
    public ${className}Entity modify(${className}ModifyRequest request) {
        ${className}Entity entity = this.findOneById(request.getId());
        AssertUtil.notNull(entity, "Error", "对象不存在");

        copyUpdatableField(entity, request);
        ${dao}.save(entity);
        return entity;
    }

    /**
     * 根据id获取对象
     */
    @Override
    public ${className}Entity findOneById(Integer id) {
        ${className}Entity entity = ${dao}.findOne(id);
        AssertUtil.notNull(entity, "Error", "对象不存在");
        AssertUtil.assertFalse(entity.getStatus() == NormalStatusNew.Deleted, "Error", "已被删除");
        return entity;
    }

    /**
     * 根据ids查询对象
     */
    @Override
    public List<${className}Entity> findListByIds(List<Integer> ids) {
        AssertUtil.notNull(ids, "Error", "ids为空");
        AssertUtil.assertFalse(ids.isEmpty(), "Error", "ids为空");

        List<SysMenuEntity> list = ${dao}.findByIdIn(ids);
        AssertUtil.notNull(list, "Error", "ids数据不存在");
        AssertUtil.assertFalse(list.isEmpty(), "Error", "ids数据不存在");

        return list;
    }

    /**
     * 根据id物理删除对象
     */
    @Override
    public void deleteOneById(Integer id) {
        //查询资源是否存在
        this.findOneById(id);
        ${dao}.deleteById(id);
    }

    /**
     * 根据ids物理删除对象
     */
    @Override
    public void deleteByIds(List<Integer> ids) {

        AssertUtil.notNull(ids, "Error", "ids为空");
        AssertUtil.assertFalse(ids.isEmpty(), "Error", "ids为空");

        List<SysMenuEntity> list = ${dao}.findByIdIn(ids);
        AssertUtil.notNull(list, "Error", "ids数据不存在");
        AssertUtil.assertFalse(list.isEmpty(), "Error", "ids数据不存在");

        AssertUtil.assertTrue(ids.size() == list.size(), "Error", "ids有数据不存在");

        ${dao}.deleteByIdIn(ids);
    }

<#if hasStatusColumn>
    /**
     * 根据id标记删除信息
     */
    @Override
    public void flagDeleteOneById(Integer id) {
        //查询资源是否存在
        SysMenuEntity entity = this.findOneById(id);
        entity.setStatus(NormalStatusNew.Deleted);
        ${dao}.save(entity);
    }

    /**
     * 根据ids标记删除信息
     */
    @Override
    public void flagDeleteBatchByIds(List<Integer> ids) {
        AssertUtil.notNull(ids, "Error", "ids为空");
        AssertUtil.assertFalse(ids.isEmpty(), "Error", "ids为空");

        List<SysMenuEntity> list = ${dao}.findByIdInAndStatus(ids, NormalStatusNew.Normal);
        AssertUtil.notNull(list, "Error", "ids数据不存在");
        AssertUtil.assertFalse(list.isEmpty(), "Error", "ids数据不存在");

        AssertUtil.assertTrue(ids.size() == list.size(), "Error", "ids有数据不存在");

        list.forEach(entity -> {
            entity.setStatus(NormalStatusNew.Deleted);
        });
        ${dao}.save(list);
    }
</#if>

    /**
     * 分页查询
     */
    @Override
    public Page<${className}Entity> pageList(${className}PageRequest request, Pageable pageable){
        return ${dao}.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
    <#list table.columns as column>
    <#if column.columnNameLower == idColumn>
    <#--  private Integer ${idColumn}; -->
    <#elseif column.isDateTimeColumn>
            if (request.get${column.columnName}Start() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("${column.columnNameLower}").as(Date.class), request.get${column.columnName}Start()));
            }
            if (request.get${column.columnName}End() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("${column.columnNameLower}").as(Date.class), request.get${column.columnName}End()));
            }

    <#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
    <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
            if (request.get${column.columnName}() != null) {
                predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(${enumName}.class), request.get${column.columnName}()));
            }
    <#else >
        <#if column.javaType?starts_with('java.lang')>
            <#if column.columnNameLower?lower_case?index_of("id")!=-1>
            if (request.get${column.columnName}s() != null && !request.get${column.columnName}s().isEmpty()) {
                if (request.get${column.columnName}s().size() == 1) {
                    predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(Integer.class), request.get${column.columnName}s().get(0)));
                } else {
                    predicates.add(root.get("${column.columnNameLower}").in(request.get${column.columnName}s()));
                }
            }
            <#elseif column.javaType=='java.lang.Integer'>
            if (request.get${column.columnName}() != null && request.get${column.columnName}() != 0) {
                predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(Integer.class), request.get${column.columnName}()));
            }
            <#elseif column.javaType=='java.lang.Long'>
            if (request.get${column.columnName}() != null && request.get${column.columnName}() != 0L) {
                predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(Integer.class), request.get${column.columnName}()));
            }
            <#elseif column.javaType=="java.lang.String">
            if (StringUtils.isNoneBlank(request.get${column.columnName}())) {
                Predicate titlePredicate = criteriaBuilder.like(root.get("${column.columnNameLower}").as(String.class), "%" + request.get${column.columnName}() + "%");
                predicates.add(criteriaBuilder.or(titlePredicate));
            }
            <#else>
            </#if>
        <#else>
            if (request.get${column.columnName}() != null) {
             predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(${column.javaType}.class), request.get${column.columnName}()));
            }
        </#if>
    </#if>

    </#list>
            Predicate[] conditions = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(conditions));
        }, pageable);
    }
}