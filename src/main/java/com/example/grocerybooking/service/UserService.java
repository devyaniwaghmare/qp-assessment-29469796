package com.example.grocerybooking.service;

import com.example.grocerybooking.dto.UserRegistrationDto;
import com.example.grocerybooking.model.Role;
import com.example.grocerybooking.model.User;
import com.example.grocerybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserRegistrationDto registrationDto, boolean isAdmin) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(isAdmin ? Role.ROLE_ADMIN : Role.ROLE_USER);
        userRepository.save(user);
    }


    public User getUserByUsername(String userName)
    {
        return userRepository.findByUsername(userName);
    }

}
