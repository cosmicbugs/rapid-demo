<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign hasDataColumn = false>
package ${basepackage}.bean.request;

import java.util.Date;
import ${project_package_prefix}.api.core.annotation.RequestApiFieldUpdatable;
import ${project_package_prefix}.dependency.web.api.core.bean.request.BaseApiBean;
<#include "/include/ybdj_include/enum_imports.include">

/**
<#include "/include/common/java_description.include">
 */
public class ${className}PageRequest implements BaseApiBean {


<#list table.columns as column>
<#if (column.columnNameLower != idColumn) && (column.remarks??) && (column.remarks?length>0)>
    /**
     * ${column.remarks}
     */
</#if>
<#if column.columnNameLower == idColumn>
  <#--  private Integer ${idColumn}; -->
<#elseif column.isDateTimeColumn>
    private Date ${column.columnNameLower}Start;

    private Date ${column.columnNameLower}End;
	<#assign hasDataColumn = true>
<#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
    <#--Java类中使用到了枚举类型.制定模板时,需要在数据库字段备注中添加信息: Enum:{EnumName}.XXX 如: Enum:YesNoNew.是否可用 -->
    <#--枚举的import需要加入到.include文件中或生成Java文件后自行引入 -->
    <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
    private ${enumName} ${column.columnNameLower};
<#else >
	<#if column.javaType?starts_with('java.lang')>
        <#if column.columnNameLower?lower_case?index_of("id")!=-1>
    private String ${column.columnNameLower}s;
        <#else>
    private ${column.javaType?substring(10)} ${column.columnNameLower};
        </#if>
	<#else>
    private ${column.javaType} ${column.columnNameLower};
	</#if>
</#if>

</#list>


<#list table.columns as column>
<#if column.columnNameLower == idColumn>
<#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
<#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
    public ${enumName} get${column.columnName}() {
    	return this.${column.columnNameLower};
    }

    public void set${column.columnName} (${enumName} ${column.columnNameLower}) {
    	this.${column.columnNameLower} = ${column.columnNameLower};
    }
<#elseif column.javaType?starts_with('java.lang')>
    public ${column.javaType?substring(10)} get${column.columnName}() {
    	return this.${column.columnNameLower};
    }

    public void set${column.columnName} (${column.javaType?substring(10)} ${column.columnNameLower}) {
    	this.${column.columnNameLower} = ${column.columnNameLower};
    }
<#elseif column.isDateTimeColumn>
    public Date get${column.columnName}Start() {
    	return this.${column.columnNameLower}Start;
    }

    public void set${column.columnName}Start (Date ${column.columnNameLower}Start) {
    	this.${column.columnNameLower}Start = ${column.columnNameLower}Start;
    }

    public Date get${column.columnName}End() {
        return this.${column.columnNameLower}End;
    }

    public void set${column.columnName}End (Date ${column.columnNameLower}End) {
        this.${column.columnNameLower}End = ${column.columnNameLower}End;
    }
<#else>
    public ${column.javaType} get${column.columnName}() {
    	return this.${column.columnNameLower};
    }

    public void set${column.columnName} (${column.javaType} ${column.columnNameLower}) {
    	this.${column.columnNameLower} = ${column.columnNameLower};
    }
</#if>

</#list>

}