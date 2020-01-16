package com.syc.china.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "tb_role")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }

}
