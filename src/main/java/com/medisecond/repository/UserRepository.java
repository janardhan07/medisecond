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

    @Query("SELECT u FROM User u WHERE u.role='DOCTOR' AND u.available=true " +
            "AND (:city IS NULL OR LOWER(u.city) LIKE LOWER(CONCAT('%',:city,'%'))) " +
            "AND (:specialty IS NULL OR LOWER(u.specialty)=LOWER(:specialty)) " +
            "AND (:area IS NULL OR LOWER(u.area) LIKE LOWER(CONCAT('%',:area,'%'))) " +
            "ORDER BY u.rating DESC, u.experienceYears DESC")
    List<User> searchDoctors(@Param("city") String city,
                             @Param("specialty") String specialty,
                             @Param("area") String area);

    @Query("SELECT DISTINCT u.city FROM User u WHERE u.role='DOCTOR' AND u.city IS NOT NULL ORDER BY u.city")
    List<String> findAllDoctorCities();

    @Query("SELECT DISTINCT u.specialty FROM User u WHERE u.role='DOCTOR' AND u.specialty IS NOT NULL ORDER BY u.specialty")
    List<String> findAllDoctorSpecialties();
}
