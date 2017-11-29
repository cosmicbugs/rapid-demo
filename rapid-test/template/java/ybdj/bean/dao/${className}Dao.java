<#assign className = table.className>
<#assign idColumn = table.idColumn>
<#assign classNameLower = className?uncap_first>
<#assign underscoreName = table.underscoreName>
package ${basepackage}.dao;

import com.sqbj.ybdj.dependency.web.api.core.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
<#include "/include/common/java_description.include">
 */
public interface ${className}Dao extends BaseDao<${className}Entity, Integer> {

    /**
     * 根据ids查询信息
     */
    List<${className}Entity> findByIdIn(List<Integer> ids);

    /**
     * 根据id删除对象
     */
    void deleteById(Integer id);

    /**
     * 根据ids删除对象
     */
    void deleteByIdIn(List<Integer> ids);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update ${underscoreName} set status= ?2 where id = ?1", nativeQuery = true)
    void updateStatusById(Integer id, String status);

    /**
     * 更新状态
     *
     * @param ids
     * @param status
     */
    @Modifying
    @Query(value = "update ${underscoreName} set status= ?2 where id in ?1", nativeQuery = true)
    void updateStatusByIds(List<Integer> ids, String status);
}