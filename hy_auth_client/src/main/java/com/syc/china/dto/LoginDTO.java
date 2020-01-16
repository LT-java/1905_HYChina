package com.syc.china.dto;

import com.syc.china.entity.User;
import lombok.Data;

/**
 *
 */
@Data
public class LoginDTO {

    private User user;

    private String accessToken;

}
