package com.syc.china.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.syc.china.mapper.RoleMapper;
import com.syc.china.pojo.PageResult;
import com.syc.china.pojo.Role;
import com.syc.china.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;


    public List<Role> queryRoles() {
        return roleMapper.selectAll();
    }

    public PageResult<Role> queryRoleList(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);

        //过滤
        Example example = new Example(Role.class);

        if(org.apache.commons.lang.StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("description","%" + key + "%")
                    .orEqualTo("role",key);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(sortBy)){
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        List<Role>  roles= roleMapper.selectByExample(example);

        // 查询
        Page<Role> pageInfo = (Page<Role>) roles;

        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public void addRole(Role role) {
        role.setCreateTime(new Date());
        roleMapper.insertSelective(role);
    }

    public void deleteRole(Integer id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    public void updateRole(Role role) {

        roleMapper.updateByPrimaryKeySelective(role);
    }
}
