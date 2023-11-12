package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.models.UserPrincipal;
import com.example.application.data.repository.UserRepository;
import com.example.application.security.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private  PasswordEncoderProvider passwordEncoderProvider;
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.search(username);
        if(users.size() == 0){
            throw new UsernameNotFoundException("User not found!");
        } else {
            return new UserPrincipal(users.get(0));
        }
    }
    public List<User> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public void saveUser(User user) {
        if (user == null) {
            System.err.println("Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        user.setPassword(passwordEncoderProvider.getPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
}
