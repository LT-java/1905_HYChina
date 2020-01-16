package com.syc.china.controller;

import com.syc.china.pojo.PageResult;
import com.syc.china.pojo.Role;
import com.syc.china.service.RoleService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;



    /**
     * 查询角色列表
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("/queryRoles")
    public ResponseEntity<PageResult<Role>> getRoleList(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "true") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){

        PageResult<Role> result = roleService.queryRoleList(page,rows,sortBy,desc,key);
        return ResponseEntity.ok(result);
    }


    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("/changeRole")
    public ResponseEntity<Void> addRole(Role role){
        roleService.addRole(role);
        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/deleteRole")
    public ResponseEntity<Void> deleteRole(@RequestParam("id")Integer id){
        roleService.deleteRole(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/changeRole")
    public ResponseEntity<Void> updateRole(Role role){
        roleService.updateRole(role);
        return ResponseEntity.ok(null);
    }

}
