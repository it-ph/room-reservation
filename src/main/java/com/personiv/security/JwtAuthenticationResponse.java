package com.personiv.security;

import java.io.Serializable;
import java.util.Date;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String access_token;
    private final String token_type;
    private final Date expiration;
    public JwtAuthenticationResponse(String access_token,Date expiration,String token_type) {
        this.access_token = access_token;
        this.expiration = expiration;
        this.token_type = token_type;
    }
 
    public String getAccess_token() {
        return access_token;
    }
    public String getToken_type() {
    	return token_type;
    }
    public Date getExpiration() {
    	return expiration;
    }
}

