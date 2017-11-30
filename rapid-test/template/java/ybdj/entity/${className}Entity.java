<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign hasDataColumn = false>
package ${basepackage}.entity;

<#include "/include/ybdj_include/entity_imports.include">
<#include "/include/ybdj_include/enum_imports.include">

/**
<#include "/include/common/java_description.include">
 */
@Entity
@Table(name = "${table.sqlName}")
public class ${className}Entity implements BaseEntity {


<#list table.columns as column>
    /**
     * ${column.remarks}
     */
<#if column.columnNameLower == idColumn>
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer ${idColumn};
<#elseif column.isDateTimeColumn>
    @Column(name = "${column.sqlName}")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ${column.columnNameLower};
	<#assign hasDataColumn = true>
<#elseif column.remarks?index_of("Enum")!=-1>
	<#--Java类中使用到了枚举类型.制定模板时,需要在数据库字段备注中添加信息: Enum:{EnumName}.XXX 如: Enum:YesNoNew.是否可用 -->
    <#--枚举的import需要加入到.include文件中或生成Java文件后自行引入 -->
    <#assign enumName= (column.remarks?substring(column.remarks?index_of("Enum:")+5,column.remarks?index_of(".")))/>
	<#if column.javaType=='java.lang.String'>
    @Enumerated(EnumType.STRING)
	<#else>
    @Enumerated(EnumType.ORDINAL)
	</#if>
    @Column(name = "${column.sqlName}")
    private ${enumName} ${column.columnNameLower};
<#else >
    @Column(name = "${column.sqlName}")
	<#if column.javaType?starts_with('java.lang')>
    private ${column.javaType?substring(10)} ${column.columnNameLower};
    <#else>
    private ${column.javaType} ${column.columnNameLower};
    </#if>
</#if>

</#list>


    @PrePersist
    public void onCreate(){
<#if hasDataColumn>
    	Date date = new Date();
<#list table.columns as column>
<#if column.columnNameLower=='createdTime'||column.columnNameLower=='createTime'||column.columnNameLower=='createdDate'||column.columnNameLower=='createDate'>
    	this.${column.columnNameLower} = date;
<#elseif column.columnNameLower=='updatedTime'||column.columnNameLower=='updateTime'||column.columnNameLower=='updatedDate'||column.columnNameLower=='updateDate'>
    	this.${column.columnNameLower} = date;
<#else>
</#if>
</#list>
</#if>
    }

    @PreUpdate
    public void onUpdate() {
<#if hasDataColumn>
<#list table.columns as column>
<#if column.columnNameLower=='updatedTime'||column.columnNameLower=='updateTime'||column.columnNameLower=='updatedDate'||column.columnNameLower=='updateDate'>
    	this.updatedTime = new Date();
</#if>
</#list>
</#if>
    }


<#list table.columns as column>
<#if column.remarks?index_of("Enum")!=-1>
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