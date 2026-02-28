package com.obntech.tenanthub.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String username;
    private List<String> roles;
    private List<String> permissions;

    public LoginResponse(String accessToken, long expiresIn, String username, List<String> roles,
                         List<String> permissions) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
    }
}
