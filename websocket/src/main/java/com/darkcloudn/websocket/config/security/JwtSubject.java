package com.darkcloudn.websocket.config.security;

import lombok.Data;

@Data
public class JwtSubject {

    private String username;

    private String businessPartnerNum;

    private String email;

    private String phoneNumber;

    private String fullName;

    private String clientId;

}
