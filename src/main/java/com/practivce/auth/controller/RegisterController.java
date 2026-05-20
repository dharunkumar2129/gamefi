package com.practivce.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practivce.auth.dto.RegisterRequest;
import com.practivce.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class RegisterController {

	private final AuthService authService;
	
	public RegisterController(AuthService authService) {
		this.authService = authService;
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request){
		try {
			authService.register(request);
			return ResponseEntity.ok("User Registered success");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
		}
	}
}
