<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign hasDataColumn = false>
package ${basepackage}.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
<#include "/include/ybdj_include/enum_imports.include">

/**
<#include "/include/common/java_description.include">
 */
public class ${className}Response {


<#list table.columns as column>
<#if (column.remarks??) && (column.remarks?length>0)>
    /**
     * ${column.remarks}
     */
</#if>
<#if column.columnNameLower == idColumn>
    	private Integer ${idColumn};
<#elseif column.isDateTimeColumn>
    <#if column.jdbcSqlTypeName=='DATE'>
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    <#elseif column.jdbcSqlTypeName=='DATETIME' || column.jdbcSqlTypeName=='TIMESTAMP'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    <#elseif column.jdbcSqlTypeName=='TIME'>
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    <#elseif column.jdbcSqlTypeName=='YEAR'>
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    <#else>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    private Date ${column.columnNameLower};
	<#assign hasDataColumn = true>
<#elseif column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
    <#--Java类中使用到了枚举类型.制定模板时,需要在数据库字段备注中添加信息: Enum:{EnumName}.XXX 如: Enum:YesNoNew.是否可用 -->
    <#--枚举的import需要加入到.include文件中或生成Java文件后自行引入 -->
    <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
    private ${enumName} ${column.columnNameLower};
<#else >
	<#if column.javaType?starts_with('java.lang')>
    private ${column.javaType?substring(10)} ${column.columnNameLower};
	<#else>
    private ${column.javaType} ${column.columnNameLower};
	</#if>
</#if>

</#list>


<#list table.columns as column>
<#if column.remarks?? && (column.remarks?index_of("Enum")!=-1)>
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
    public Date get${column.columnName}() {
    	return this.${column.columnNameLower};
    }

    public void set${column.columnName} (Date ${column.columnNameLower}) {
    	this.${column.columnNameLower} = ${column.columnNameLower};
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