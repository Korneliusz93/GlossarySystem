package com.example.application.data.models;

import com.example.application.data.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.user.getRole().equals("USER")){
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }else if (this.user.getRole().equals("ADMIN")){
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else if (this.user.getRole().equals("STAFF")){
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_STAFF"));
        }else if (this.user.getRole().equals("GUEST")){
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
