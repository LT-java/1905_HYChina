package com.syc.china.mapper;

import com.syc.china.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {


    /**
     * 添加用户角色中间表关系
     * @param userId
     * @param roleId
     */
    @Insert("INSERT INTO tb_user_role (user_id, role_id) VALUES(#{userId},#{roleId})")
    int insertMid(@Param("userId") Integer userId, @Param("roleId") Integer roleId);


    /**
     * 删除用户角色中间表关系
     * @param userId
     */
    @Delete("delete from tb_user_role where user_id = #{userId}")
    int deleteMid(@Param("userId") Integer userId);


    @Select("select decription from tb_role where id = (select ur.role_id from tb_user_role ur where ur.user_id= #{id}) ")
    String queryRoleName(@Param("id")Integer id);


}
