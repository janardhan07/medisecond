package com.medisecond.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String phoneNumber;
    private String fullName;
    private String gender;

    // ── Doctor profile ──────────────────────────────────────────
    private String specialty;
    private String city;
    private String area;
    private String clinicName;
    private String clinicAddress;
    private Integer experienceYears;
    private Integer consultationFee;
    @Column(columnDefinition = "TEXT")
    private String bio;
    private String qualifications;
    private String languages;
    @Builder.Default
    private Double rating = 0.0;
    @Builder.Default
    private Integer ratingCount = 0;
    @Builder.Default
    private Boolean available = true;
    @Builder.Default
    private Boolean onlineConsultation = false;

    // ── Patient profile ─────────────────────────────────────────
    private Integer age;
    private String bloodGroup;
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    public enum Role {PATIENT, DOCTOR, ADMIN}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
