package com.gdc.avp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gwm.avp.entity.Resource;
import com.gwm.avp.entity.RoleTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleTemplateMapper extends BaseMapper<RoleTemplate> {


    @Insert({"<script>",
            "insert into role_template (role_id, template_code,operator) ",
            "values(#{roleId},#{templateCode},#{operator})",
            "</script>" })
    void saveRoleTemplates(@Param("operator")String operator,@Param("roleId") Long roleId,@Param("templateCode") Long templateCode);


    @Delete({"<script>",
            "delete from role_template where role_id = #{roleId}",
            "</script>"})
    void deleteRoleTemplate(@Param("roleId") Long roleId);

    @Select({"<script>",
            "select template_code from role_template where role_id = #{roleId}",
            "</script>"})
    List<String> getSelectedTemplateCodeByRoleId(@Param("roleId") Long roleId);

    @Select({"<script>",
            "SELECT\n" +
                    "\trt.template_code \n" +
                    "FROM\n" +
                    "\trole_template rt\n" +
                    "\tLEFT JOIN role ro ON rt.role_id = ro.id\n" +
                    "\tLEFT JOIN user ue ON ue.role_id = ro.id \n" +
                    "WHERE\n" +
                    "\tue.user_name = #{userName}",
            "</script>"})
    List<String> findTemplateCodeListByUserName(@Param("userName") String userName);

    @Select({"<script>",
            "SELECT\n" +
                    "\trt.id, \n" +
                    "\trt.role_id, \n" +
                    "\trt.template_code, \n" +
                    "\trt.create_time \n" +
                    "FROM\n" +
                    "\trole_template rt\n" +
                    "\tLEFT JOIN role ro ON rt.role_id = ro.id\n" +
                    "\tLEFT JOIN user ue ON ue.role_id = ro.id \n" +
                    "WHERE\n" +
                    "\tue.user_name = #{userName}",
            "</script>"})
    List<RoleTemplate> findByPage(Page<RoleTemplate> page,@Param("userName")String userName);
}
