package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.models.UserPrincipal;
import com.example.application.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.search(username);

        if(users.size() == 0){
            throw new UsernameNotFoundException("User not found!");
        }else {
            return new UserPrincipal(users.get(0));
        }

    }
}
