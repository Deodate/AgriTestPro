package com.AgriTest.util;

import com.AgriTest.security.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
@Component("securityUtils")  // Add this annotation to make it a Spring bean with name "securityUtils"
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
     * Note: This method assumes the Authentication principal contains a UserDetailsImpl object with an ID field.
     * 
     * @return the user ID of the current user, or null if not available
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
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
    
    /**
     * Check if the current user ID matches the given user ID.
     * Useful for authorization checks when a user should only access their own resources.
     *
     * @param id the user ID to check against
     * @return true if the current user ID matches the given ID
     */
    public boolean isCurrentUserId(Long id) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(id);
    }
    
    /**
     * Check if the current user ID matches the given user ID or if the current username matches the given username.
     * Useful for authorization checks when a user should only access their own resources.
     *
     * @param identifier the user ID or username to check against
     * @return true if the current user matches the given identifier
     */
    public boolean isCurrentUserIdentifier(String identifier) {
        // Check if the identifier is numeric (userId)
        if (identifier.matches("\\d+")) {
            Long userId = Long.valueOf(identifier);
            Long currentUserId = getCurrentUserId();
            return currentUserId != null && currentUserId.equals(userId);
        } else {
            // Assume it's a username
            Optional<String> currentUsername = getCurrentUsername();
            return currentUsername.isPresent() && currentUsername.get().equals(identifier);
        }
    }
}