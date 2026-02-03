package com.practivce.auth.entity; // Keep your package name

import jakarta.persistence.*;
import com.practivce.auth.entity.Submissions; // Import your Submissions entity if it's in a different package
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "verification_code")
    private String verificationCode;

    // --- AUTH FIELDS (Existing) ---
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    
    @Transient // Not saved to DB, just for returning token
    private String accessToken;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // --- GAME FIELDS (New Additions) ---

    @Column(unique = true, nullable = false)
    private String username; // Needed for Leaderboard

    private Long xp = 0L; // Default to 0 Experience Points

    private int level = 1; // Default Level 1

    // Link to their coding history
    // "mappedBy" refers to the 'user' field in Submissions.java
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Submissions> submissions = new ArrayList<>();

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // Game Getters/Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getXp() { return xp; }
    public void setXp(Long xp) { this.xp = xp; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public List<Submissions> getSubmissions() { return submissions; }
    public void setSubmissions(List<Submissions> submissions) { this.submissions = submissions; }
}