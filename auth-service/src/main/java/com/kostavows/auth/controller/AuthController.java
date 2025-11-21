package com.kostavows.auth.controller;          


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.kostavows.auth.entity.User;
import com.kostavows.auth.repository.UserRepository;
import com.kostavows.auth.service.JwtService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setEmail(request.get("email"));
        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getUsername());
        return ResponseEntity.ok(Map.of("token", token, "userId", saved.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        String token = jwtService.generateToken(authentication.getName());
        User user = userRepository.findByUsername(request.get("username")).orElse(null);
        return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
    }
}