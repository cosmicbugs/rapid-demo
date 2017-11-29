<#assign className = table.className>
<#assign classNameFirstLower = table.classNameFirstLower>
<#assign idColumn = table.idColumn>
<#assign dao = classNameFirstLower+"Dao">
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.impl;

import com.sqbj.ybdj.dependency.web.api.core.service.AbstractBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.criteria.Predicate;
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
        rerturn entity;
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
        rerturn entity;
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
        return ${dao}.findByIdIn(ids);
    }

    /**
     * 根据id物理删除对象
     */
    @Override
    public void deleteOneById(Integer id) {
        ${dao}.deleteById(id);
    }

    /**
     * 根据ids物理删除对象
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        ${dao}.deleteByIdIn(ids);
    }

    /**
     * 根据id标记删除信息
     */
    @Override
    public void flagDeleteOneById(Integer id) {
        ${dao}.updateStatusById(id, NormalStatusNew.Deleted.name());
    }

    /**
     * 根据ids标记删除信息
     */
    @Override
    public void flagDeleteBatchByIds(List<Integer> ids) {
        ${dao}.updateStatusByIds(id, NormalStatusNew.Deleted.name());
    }

    /**
     * 分页查询
     */
    @Override
    Page<${className}Entity> pageList(${className}PageRequest request, Pageable pageable){
        return courseInfoDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
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
            if (request.get${enumName}() != null) {
                predicates.add(criteriaBuilder.equal(root.get("${column.columnNameLower}").as(${enumName}.class), request.get${enumName}()));
            }
    <#else >
        <#if column.javaType[0..8]=='java.lang'>
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