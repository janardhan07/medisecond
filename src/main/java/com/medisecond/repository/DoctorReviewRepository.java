package com.medisecond.repository;

import com.medisecond.model.DoctorReview;
import com.medisecond.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorReviewRepository extends JpaRepository<DoctorReview, Long> {
    List<DoctorReview> findByDoctorOrderByCreatedAtDesc(User doctor);
    Optional<DoctorReview> findByDoctorAndPatient(User doctor, User patient);
    boolean existsByDoctorAndPatient(User doctor, User patient);
}
