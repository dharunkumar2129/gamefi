package com.practivce.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practivce.auth.entity.User;
import com.practivce.auth.repository.UserRepository;


	@RestController
	@RequestMapping("/api")
	public class UserController {
		
		@Autowired
		private UserRepository userRepository;
		
		
		@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_STUDENT')")
		@GetMapping("/users")
		public List<User> getUsers() {
		    return userRepository.findAll();
		}

		
	
	    @PostMapping
	    public User createUser(@RequestBody User user) {
	        return userRepository.save(user);
	    }
	    
	    
	    @GetMapping("/{id}")
	    public User getUserById(@PathVariable Long id) {
	        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    }

	
	    @PutMapping("/{id}")
	    public User updateUser(@PathVariable Long id, @RequestBody User user) {
	    	User userData = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    	userData.setEmail(user.getEmail());
	    	return userRepository.save(userData);
	    }


	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
	    	User userData = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    	userRepository.delete(userData);
	    	return ResponseEntity.ok().build();
	    }
	}
