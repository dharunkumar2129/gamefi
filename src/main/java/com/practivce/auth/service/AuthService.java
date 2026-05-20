package com.practivce.auth.service;

import com.practivce.auth.entity.Leaderboard;
import com.practivce.auth.repository.LeaderboardRepository;
import com.practivce.auth.dto.LoginRequest;
import com.practivce.auth.dto.RegisterRequest;
import com.practivce.auth.entity.Role;
import com.practivce.auth.entity.User;
import com.practivce.auth.repository.RoleRepository;
import com.practivce.auth.repository.UserRepository;
import com.practivce.auth.security.JwtUtil;

import java.util.Set;
// Random for OTP generation
import java.util.Random;

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
    private final LeaderboardRepository leaderboardRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService; // <--- ADDED EmailService

    // Constructor Injection
    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            LeaderboardRepository leaderboardRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            EmailService emailService // <--- INJECT EmailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.leaderboardRepository = leaderboardRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        // Check if user verified their email
        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not verified. Please check your email for the OTP.");
        }

        return jwtUtil.generateAccessToken(user);
    }

    @Transactional
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        Role userRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_STUDENT");
                    return roleRepository.save(role);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));

        user.setXp(0L);
        user.setLevel(1);

        // OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setVerificationCode(otp);
        user.setEnabled(false);

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), otp);

        return "OTP sent to email. Please verify.";
    }

    @Transactional
    public void verifyUser(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User is already verified.");
        }

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            // 1. Enable the User
            user.setEnabled(true);
            user.setVerificationCode(null); // Clear the code so it can't be reused
            userRepository.save(user);

            // 2. Create Leaderboard Entry (Only now!)
            Leaderboard lb = new Leaderboard();
            lb.setUser(user);
            lb.setTotalScore(0L);
            lb.setProblemsSolved(0);
            leaderboardRepository.save(lb);
        } else {
            throw new RuntimeException("Invalid Verification Code");
        }
    }
}