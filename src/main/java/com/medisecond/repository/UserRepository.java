package com.medisecond.repository;

import com.medisecond.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // ── Doctor search queries ─────────────────────────────────────────────────

    /** Find doctors by city (case-insensitive partial match) */
    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' " +
           "AND LOWER(u.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND u.available = true " +
           "ORDER BY u.rating DESC, u.experienceYears DESC")
    List<User> findDoctorsByCity(@Param("city") String city);

    /** Find doctors by city AND specialty */
    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' " +
           "AND LOWER(u.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND LOWER(u.specialty) = LOWER(:specialty) " +
           "AND u.available = true " +
           "ORDER BY u.rating DESC, u.experienceYears DESC")
    List<User> findDoctorsByCityAndSpecialty(@Param("city") String city,
                                              @Param("specialty") String specialty);

    /** Find doctors by specialty only */
    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' " +
           "AND LOWER(u.specialty) = LOWER(:specialty) " +
           "AND u.available = true " +
           "ORDER BY u.rating DESC, u.experienceYears DESC")
    List<User> findDoctorsBySpecialty(@Param("specialty") String specialty);

    /** Get all unique cities that have at least one available doctor */
    @Query("SELECT DISTINCT u.city FROM User u WHERE u.role = 'DOCTOR' " +
           "AND u.city IS NOT NULL AND u.available = true ORDER BY u.city")
    List<String> findAllDoctorCities();

    /** All available doctors sorted by rating */
    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' AND u.available = true " +
           "ORDER BY u.rating DESC, u.experienceYears DESC")
    List<User> findAllAvailableDoctors();
}
