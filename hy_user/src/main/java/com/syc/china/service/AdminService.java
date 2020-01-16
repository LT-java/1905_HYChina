package com.syc.china.service;

import com.syc.china.mapper.AdminMapper;
import com.syc.china.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;


    public Admin login(Admin admin) {
        return adminMapper.selectOne(admin);
    }
}


