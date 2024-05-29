package com.example.grocerybooking.controller;

import com.example.grocerybooking.dto.UserRegistrationDto;
import com.example.grocerybooking.model.User;
import com.example.grocerybooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationController(UserService userService,AuthenticationManager authenticationManager)
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userRegistrationDto,@RequestParam(defaultValue = "false") boolean isAdmin)
    {
           if(userService.getUserByUsername(userRegistrationDto.getUsername()) != null)
           {
               return ResponseEntity.badRequest().body("Username is already taken.");
           }
           userService.register(userRegistrationDto,isAdmin);
           return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRegistrationDto logInDto)
    {
        Authentication authentication =  authenticationManager.
                                              authenticate(new UsernamePasswordAuthenticationToken(logInDto.getUsername(),logInDto.getPassword()));
        System.out.println(logInDto.getUsername()+" "+logInDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User logged in successfully");
    }


}
