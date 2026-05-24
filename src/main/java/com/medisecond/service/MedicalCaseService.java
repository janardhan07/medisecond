package com.medisecond.service;

import com.medisecond.dto.MedicalCaseRequest;
import com.medisecond.dto.MedicalCaseResponse;
import com.medisecond.model.MedicalCase;
import com.medisecond.model.User;
import com.medisecond.repository.MedicalCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalCaseService {

    private final MedicalCaseRepository caseRepository;
    private final MlService mlService;

    public List<MedicalCaseResponse> getCasesForUser(User user) {
        return switch (user.getRole()) {
            case PATIENT -> caseRepository.findByPatientOrderByCreatedAtDesc(user)
                    .stream().map(MedicalCaseResponse::from).toList();
            case DOCTOR  -> caseRepository.findByAssignedDoctorOrderByCreatedAtDesc(user)
                    .stream().map(MedicalCaseResponse::from).toList();
            case ADMIN   -> caseRepository.findAll()
                    .stream().map(MedicalCaseResponse::from).toList();
        };
    }

    public MedicalCaseResponse createCase(MedicalCaseRequest req, User patient) {
        MlService.Prediction prediction = mlService.predict(req.getSymptoms());

        MedicalCase medicalCase = MedicalCase.builder()
                .patient(patient)
                .title(req.getTitle())
                .description(req.getDescription())
                .symptoms(req.getSymptoms())
                .mlPredictedCategory(prediction.specialty())
                .mlConfidenceScore(prediction.confidence())
                .status(MedicalCase.Status.PENDING)
                .build();

        return MedicalCaseResponse.from(caseRepository.save(medicalCase));
    }

    public MedicalCaseResponse getCaseById(Long id, User user) {
        MedicalCase c = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found"));
        checkAccess(c, user);
        return MedicalCaseResponse.from(c);
    }

    public MedicalCase getCaseEntityById(Long id) {
        return caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found"));
    }

    private void checkAccess(MedicalCase c, User user) {
        boolean allowed = switch (user.getRole()) {
            case PATIENT -> c.getPatient().getId().equals(user.getId());
            case DOCTOR  -> c.getAssignedDoctor() != null && c.getAssignedDoctor().getId().equals(user.getId());
            case ADMIN   -> true;
        };
        if (!allowed) throw new AccessDeniedException("Access denied");
    }
}
