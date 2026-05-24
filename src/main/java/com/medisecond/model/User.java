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
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

    // ── Doctor profile fields ────────────────────────────────────────────────

    /** Medical specialty e.g. "Cardiologist", "Neurologist" */
    private String specialty;

    /** City / area the doctor operates from e.g. "Mumbai", "Delhi - Connaught Place" */
    private String city;

    /** Full clinic address */
    private String clinicAddress;

    /** Years of experience */
    private Integer experienceYears;

    /** Consultation fee in INR */
    private Integer consultationFee;

    /** Short bio / qualifications */
    @Column(columnDefinition = "TEXT")
    private String bio;

    /** Average rating 0.0–5.0, updated when patients submit reviews */
    @Builder.Default
    private Double rating = 0.0;

    /** Total number of ratings received */
    @Builder.Default
    private Integer ratingCount = 0;

    /** Whether the doctor is accepting new patients */
    @Builder.Default
    private Boolean available = true;

    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
