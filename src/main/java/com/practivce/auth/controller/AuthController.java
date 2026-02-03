package com.practivce.auth.controller; // Ensure this matches your package name

import com.practivce.auth.dto.LoginRequest;
import com.practivce.auth.entity.User;
import com.practivce.auth.repository.UserRepository;
import com.practivce.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository; // 1. Added Repository

    // 2. Updated Constructor to inject UserRepository
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String code) {
        try {
            authService.verifyUser(email, code);
            return ResponseEntity.ok("Verification successful! You can now login.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) { // Return type is generic <?> or <Map>
        try {
            // 3. Get the Token (Your existing logic)
            String token = authService.login(request);

            // 4. Get the User Details (The Critical Fix)
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 5. Construct a JSON Object Response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getId()); // <--- This sends the number (e.g., 52)
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // Return JSON error for consistency
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid email or password");
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}


//package com.practivce.auth.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//import com.practivce.auth.dto.LoginRequest;
//import com.practivce.auth.service.AuthService;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
//
//        try {
//            String token = authService.login(request);
//            return ResponseEntity.ok(token);
//        } catch (AuthenticationException e) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        }
//    }
//}

//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//	
//	
//
//	@Autowired
//	private JwtUtil jwtUtil;
//
//	@Autowired
//	private AuthService authService;
//
//	@PostMapping("/login")
//	public ResponseEntity<LoginResponse> login(
//	        @Valid @RequestBody LoginRequest request) {
//
//	    String token = authService.login(request);
//	    return ResponseEntity.ok(new LoginResponse(token));
//	}
//
//}