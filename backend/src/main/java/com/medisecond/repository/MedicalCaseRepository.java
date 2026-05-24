package com.medisecond.repository;

import com.medisecond.model.MedicalCase;
import com.medisecond.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalCaseRepository extends JpaRepository<MedicalCase, Long> {
    List<MedicalCase> findByPatientOrderByCreatedAtDesc(User patient);
    List<MedicalCase> findByAssignedDoctorOrderByCreatedAtDesc(User doctor);
}
