package com.syc.china.dao;

import com.syc.china.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long> {

    User findByUsername(String username);

}
