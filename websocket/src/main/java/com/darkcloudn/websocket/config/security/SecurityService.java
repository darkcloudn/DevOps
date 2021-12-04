package com.darkcloudn.websocket.config.security;

import com.darkcloudn.common.exception.AuthorizationException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * @Description:
 * @Author: darkcloudn
 * @Date: 9/30/2021
 */

@Component
public class SecurityService {

    @Value("${darkcloudn.tokenType}")
    private String tokenType;

    @Value("${darkcloudn.Authorization}")
    private String keyHeaderAuthor;

    public String getToken(HttpServletRequest request) {
        String strTokenType = tokenType.concat(" ");
        String authHeader = request.getHeader(keyHeaderAuthor);
        if (authHeader != null && authHeader.startsWith(strTokenType)) {
            return authHeader.replace(strTokenType, "");
        }
        return null;
    }

    public String getToken(String authHeader) {
        String strTokenType = tokenType.concat(" ");
        if (authHeader != null && authHeader.startsWith(strTokenType)) {
            return authHeader.replace(strTokenType, "");
        }
        return null;
    }

    public void handleAuthorization(String username, String partnerNum) {

        UserPrinciple userPrinciple = getCurrentUser();

        if (userPrinciple != null) {
            boolean check = false;
            if (StringUtils.isNotEmpty(userPrinciple.getUsername())) {
                check = userPrinciple.getUsername().equals(username);
            }

            if (!check) {
                throw new AuthorizationException();
            }
        }

    }

    public void handleAuthorization(String username) {
        handleAuthorization(username, "");
    }

    public void handleAuthorizationByPartnerNum(String partnerNum) {
        handleAuthorization("", partnerNum);
    }

    public UserPrinciple getCurrentUser() {
        return this.getCurrentUser(true); 
    }

    public UserPrinciple getCurrentUser(boolean throwError) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            if(throwError) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error -> Unauthorized");
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            if(throwError) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error -> Unauthorized");
            return null;
        }
        UserPrinciple userPrinciple = null;
        if (principal instanceof UserPrinciple) {
            userPrinciple = (UserPrinciple) principal;
        }
        return userPrinciple;
    }
}
