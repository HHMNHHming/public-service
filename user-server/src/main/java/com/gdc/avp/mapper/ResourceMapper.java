package com.gdc.avp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gwm.avp.entity.Resource;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据角色id获取资源信息
     * @param roleId
     * @param type 0：菜单；1：普通资源
     * @param isParent 0：其他资源；1：顶级菜单；其他或不传获取全部
     * @return
     */
    @Select({"<script>",
            "select * " ,
            "from resource r " ,
            "left join role_resource rr on r.id = rr.resource_id " ,
            "where rr.role_id = #{roleId}" ,
//            "<if test=\"type!=null and type!=''\">",
            "and r.type = #{type}",
//            "</if>",
            "<if test=\"isParent==1\">",
            "and r.parent_id is null",
            "</if>",
            "<if test=\"isParent==0\">",
            "and r.parent_id is not null ",
            "order by r.sort",
            "</if>",
            "</script>" })
    List<Resource> getResourceByRoleId(@Param("roleId") Long roleId, @Param("type") Integer type
    ,@Param("isParent") Integer isParent);

    @Insert({"<script>",
            "insert into role_resource (role_id, resource_id) ",
            "values(${roleId},${resourceId})",
            "</script>" })
    void saveRoleResource(@Param("roleId") Long roleId,@Param("resourceId") Long resourceId);

    @Insert({"<script>",
            "insert into role_resource (role_id, resource_id) values",
            "<foreach collection='resourceIds' item='resourceId'   separator=','> ",
            "(#{roleId},#{resourceId})",
            "</foreach> ",
            "</script>" })
    void saveRoleResourceBatch(@Param("roleId") Long roleId,@Param("resourceIds") Long[] resourceIds);
    @Select({"<script>",
            "select name from role where exists ",
            "(select * from role_resource where role_resource.role_id = role.id ",
            "and role_resource.resource_id = ${resourceId})",
            "</script>"})
    List<String> getRoleNameByResourceId(@Param("resourceId") Long resourceId);

    @Delete({"<script>",
            "delete from role_resource where role_id = ${roleId}",
            "</script>"})
    void deleteRoleResource(@Param("roleId") Long roleID);

    @Select({"<script>",
            "select resource_id from role_resource where role_id = ${roleId}",
            "</script>"})
    List<Long> getSelectedResourceIdByRoleId(@Param("roleId") Long roleId);

    @Select({"<script>",
            "select r.*,GROUP_CONCAT(r2.name) roleNames from resource r left join role_resource rr on r.id = rr.resource_id left join `role` r2 on rr.role_id = r2.id where r.path is not null group by r.id",
    "</script>"})
    List<Map<String,Object>> findAllWithRole();
}
