<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.enums;

import ${project_package_prefix}.api.core.bean.common.PageOrder;

/**
 * 分页查询排序
 <#include "/include/common/java_description.include">
 */
public enum ${className}Order implements PageOrder {

<#list table.columns as column>
<#if column.isDateTimeColumn>
    //${column.remarks}
    ${column.constantName}("${column.sqlName}", "${column.columnNameLower}"),
<#else>
	<#if column.columnNameLowerCase?contains("top") || column.columnNameLowerCase?contains("orders") || column.columnNameLowerCase?contains("sort") || column.columnNameLowerCase?contains("sort")
        || column.columnNameLowerCase?contains("views") || column.columnNameLowerCase?contains("clicks")>
    //${column.remarks}
    ${column.constantName}("${column.sqlName}", "${column.columnNameLower}"),
	</#if>
</#if>
</#list>
    ;

    private String dbColumn;

    private String entityColumn;

    ${className}Order(String dbColumn, String entityColumn) {
        this.dbColumn = dbColumn;
        this.entityColumn = entityColumn;
    }

    /**
     * 根据 枚举变量 获取 枚举中DbColumn值
     *
     * @return
     */
    @Override
    public String getDbColumn() {
        return this.dbColumn;
    }

    /**
     * 根据 枚举变量 获取 枚举中EntityColumn值
     *
     * @return
     */
    @Override
    public String getEntityColumn() {
        return this.entityColumn;
    }

}