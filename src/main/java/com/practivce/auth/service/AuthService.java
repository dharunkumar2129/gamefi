package com.practivce.auth.service;

import com.practivce.auth.entity.Leaderboard;
import com.practivce.auth.repository.LeaderboardRepository; // Import Game Repo

import com.practivce.auth.dto.LoginRequest;
import com.practivce.auth.dto.RegisterRequest;
import com.practivce.auth.entity.Role;
import com.practivce.auth.entity.User;
import com.practivce.auth.repository.RoleRepository;
import com.practivce.auth.repository.UserRepository;
import com.practivce.auth.security.JwtUtil;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LeaderboardRepository leaderboardRepository; // <--- ADD THIS
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Constructor Injection
    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            LeaderboardRepository leaderboardRepository, // <--- ADD THIS
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.leaderboardRepository = leaderboardRepository; // <--- ADD THIS
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return jwtUtil.generateAccessToken(user);
    }

    @Transactional // Ensures User and Leaderboard are saved together (or neither is)
    public void register(RegisterRequest request) {
        // 1. Check if Email Exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Check if Role Exists (Make sure "ROLE_STUDENT" is in your DB)
        Role userRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_STUDENT' not found."));

        // 3. Create the User
        User user = new User();
        user.setUsername(request.getUsername()); // <--- CRITICAL: Save the username!
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));
        
        // Game defaults
        user.setXp(0L); 
        user.setLevel(1);

        // 4. Save User to Generate ID
        User savedUser = userRepository.save(user);

        // 5. Create Game Leaderboard Entry (The Connection)
        Leaderboard lb = new Leaderboard();
        lb.setUser(savedUser);       // Link to the new user
        lb.setTotalScore(0L);        // Start with 0 score
        lb.setProblemsSolved(0);     // 0 problems solved
        
        leaderboardRepository.save(lb); // Save to Leaderboard table
    }
}