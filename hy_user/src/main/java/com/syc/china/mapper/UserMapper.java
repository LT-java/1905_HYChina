package com.syc.china.mapper;

import com.syc.china.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select("select * from tb_user where username=#{username}")
    List<User> selectUserByName(@Param("username") String username);

    /*根据用户名查询对应的角色名称*/
    @Select("select r.role from tb_user u join tb_role r on u.member_id=r.id where u.username=#{username}")
    String selectRoleByUsername(@Param("username") String username);

    @Select("select salt from tb_user where username=#{username}")
    String selectSaltByUsername(String username);

    @Select("select password from tb_user where username=#{username}")
    String getPassword(String username);

    @Select("select decription from tb_role where id = (select role_id from tb_user_role where user_id = #{userId})")
    String queryMemberName(@Param("userId") Integer userId);

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
}
