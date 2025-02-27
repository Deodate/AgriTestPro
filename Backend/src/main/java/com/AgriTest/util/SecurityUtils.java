package com.AgriTest.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.AgriTest.service.UserDetailsImpl;

import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public class SecurityUtils {

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return Optional.empty();
        }
        
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return Optional.ofNullable(springSecurityUser.getUsername());
        }
        
        if (authentication.getPrincipal() instanceof String) {
            return Optional.ofNullable((String) authentication.getPrincipal());
        }
        
        return Optional.empty();
    }
    
    /**
     * Get the user ID of the current user.
     * Note: This method assumes the Authentication principal contains a User object with an ID field.
     * 
     * @return the user ID of the current user, or null if not available
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getId();
        }
        
        return null;
    }
    
    /**
     * Check if the current user has a specific authority.
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean hasCurrentUserAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
    
    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
}