package com.medisecond.service;

import com.medisecond.dto.AiSymptomResponse;
import com.medisecond.dto.UserDto;
import com.medisecond.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    private final UserRepository userRepo;

    // Weighted keyword map for accurate specialty prediction
    private static final List<Object[]> KEYWORDS = List.of(
            new Object[]{"chest pain", 3, "Cardiologist"}, new Object[]{"heart attack", 4, "Cardiologist"},
            new Object[]{"palpitation", 3, "Cardiologist"}, new Object[]{"shortness of breath", 2, "Cardiologist"},
            new Object[]{"hypertension", 3, "Cardiologist"}, new Object[]{"high blood pressure", 2, "Cardiologist"},
            new Object[]{"headache", 2, "Neurologist"}, new Object[]{"migraine", 3, "Neurologist"},
            new Object[]{"seizure", 4, "Neurologist"}, new Object[]{"dizziness", 2, "Neurologist"},
            new Object[]{"numbness", 2, "Neurologist"}, new Object[]{"memory loss", 3, "Neurologist"},
            new Object[]{"stroke", 4, "Neurologist"}, new Object[]{"tremor", 3, "Neurologist"},
            new Object[]{"blurred vision", 3, "Ophthalmologist"}, new Object[]{"eye pain", 3, "Ophthalmologist"},
            new Object[]{"vision loss", 4, "Ophthalmologist"}, new Object[]{"red eye", 2, "Ophthalmologist"},
            new Object[]{"floaters", 3, "Ophthalmologist"}, new Object[]{"double vision", 3, "Ophthalmologist"},
            new Object[]{"stomach ache", 2, "Gastroenterologist"}, new Object[]{"nausea", 2, "Gastroenterologist"},
            new Object[]{"vomiting", 2, "Gastroenterologist"}, new Object[]{"diarrhea", 3, "Gastroenterologist"},
            new Object[]{"constipation", 2, "Gastroenterologist"}, new Object[]{"acid reflux", 3, "Gastroenterologist"},
            new Object[]{"abdominal pain", 2, "Gastroenterologist"}, new Object[]{"bloating", 2, "Gastroenterologist"},
            new Object[]{"skin rash", 3, "Dermatologist"}, new Object[]{"itching", 2, "Dermatologist"},
            new Object[]{"acne", 2, "Dermatologist"}, new Object[]{"eczema", 3, "Dermatologist"},
            new Object[]{"hair loss", 2, "Dermatologist"}, new Object[]{"psoriasis", 3, "Dermatologist"},
            new Object[]{"joint pain", 3, "Orthopedist"}, new Object[]{"back pain", 2, "Orthopedist"},
            new Object[]{"knee pain", 3, "Orthopedist"}, new Object[]{"fracture", 4, "Orthopedist"},
            new Object[]{"bone pain", 3, "Orthopedist"}, new Object[]{"arthritis", 3, "Orthopedist"},
            new Object[]{"ear pain", 3, "ENT Specialist"}, new Object[]{"hearing loss", 3, "ENT Specialist"},
            new Object[]{"sore throat", 2, "ENT Specialist"}, new Object[]{"sinusitis", 3, "ENT Specialist"},
            new Object[]{"nasal congestion", 2, "ENT Specialist"}, new Object[]{"tonsil", 2, "ENT Specialist"},
            new Object[]{"periods", 3, "Gynecologist"}, new Object[]{"menstrual", 3, "Gynecologist"},
            new Object[]{"pregnancy", 4, "Gynecologist"}, new Object[]{"pcos", 3, "Gynecologist"},
            new Object[]{"vaginal", 3, "Gynecologist"}, new Object[]{"ovarian", 3, "Gynecologist"},
            new Object[]{"child", 2, "Pediatrician"}, new Object[]{"baby", 2, "Pediatrician"},
            new Object[]{"infant", 3, "Pediatrician"}, new Object[]{"vaccination", 2, "Pediatrician"},
            new Object[]{"anxiety", 3, "Psychiatrist"}, new Object[]{"depression", 3, "Psychiatrist"},
            new Object[]{"mental", 2, "Psychiatrist"}, new Object[]{"insomnia", 2, "Psychiatrist"},
            new Object[]{"panic attack", 3, "Psychiatrist"}, new Object[]{"stress", 2, "Psychiatrist"},
            new Object[]{"fever", 1, "General Physician"}, new Object[]{"cough", 1, "General Physician"},
            new Object[]{"cold", 1, "General Physician"}, new Object[]{"fatigue", 1, "General Physician"},
            new Object[]{"weakness", 1, "General Physician"}, new Object[]{"body ache", 1, "General Physician"}
    );

    private static final Map<String, String> SPECIALTY_REASONS = Map.ofEntries(
            Map.entry("Cardiologist", "Your symptoms suggest a heart or cardiovascular condition that needs specialist evaluation."),
            Map.entry("Neurologist", "Your symptoms indicate a possible neurological issue that requires expert assessment."),
            Map.entry("Ophthalmologist", "Your symptoms point to an eye condition that needs an eye specialist."),
            Map.entry("Gastroenterologist", "Your symptoms suggest a digestive system issue requiring specialist care."),
            Map.entry("Dermatologist", "Your symptoms indicate a skin condition best evaluated by a dermatologist."),
            Map.entry("Orthopedist", "Your symptoms suggest a bone, joint, or muscle issue needing specialist care."),
            Map.entry("ENT Specialist", "Your symptoms relate to ear, nose, or throat and need an ENT evaluation."),
            Map.entry("Gynecologist", "Your symptoms suggest a women's health issue requiring specialist care."),
            Map.entry("Pediatrician", "Symptoms in children require evaluation by a pediatric specialist."),
            Map.entry("Psychiatrist", "Your symptoms may relate to mental health and benefit from specialist support."),
            Map.entry("General Physician", "A general physician can evaluate your symptoms and refer you if needed.")
    );

    public AiSymptomResponse analyze(String symptoms, String city) {
        if (symptoms == null || symptoms.isBlank()) {
            return AiSymptomResponse.builder().suggestedSpecialty("General Physician")
                    .reasoning("Please describe your symptoms for a more accurate suggestion.").confidence(0.5).build();
        }
        String lower = symptoms.toLowerCase();
        Map<String, Integer> scores = new LinkedHashMap<>();
        for (Object[] k : KEYWORDS) {
            if (lower.contains((String) k[0])) {
                scores.merge((String) k[2], (Integer) k[1], Integer::sum);
            }
        }
        if (scores.isEmpty()) {
            return AiSymptomResponse.builder().suggestedSpecialty("General Physician")
                    .reasoning("Based on your symptoms, a general physician can help diagnose and refer you to the right specialist.")
                    .confidence(0.5).recommendedDoctors(getDoctors("General Physician", city)).build();
        }
        // Top specialty
        String top = scores.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("General Physician");
        int topScore = scores.get(top);
        int total = scores.values().stream().mapToInt(i -> i).sum();
        double conf = Math.min(0.95, 0.45 + (0.5 * (double) topScore / (total + 2)));
        conf = Math.round(conf * 100.0) / 100.0;
        // Alternatives
        List<String> alts = scores.entrySet().stream()
                .filter(e -> !e.getKey().equals(top)).sorted((a, b) -> b.getValue() - a.getValue())
                .map(Map.Entry::getKey).limit(2).toList();
        return AiSymptomResponse.builder()
                .suggestedSpecialty(top)
                .reasoning(SPECIALTY_REASONS.getOrDefault(top, "Based on your symptoms, this specialist is recommended."))
                .confidence(conf).alternativeSpecialties(alts)
                .recommendedDoctors(getDoctors(top, city)).build();
    }

    private List<UserDto> getDoctors(String specialty, String city) {
        return userRepo.searchDoctors(
                (city != null && !city.isBlank()) ? city : null, specialty, null
        ).stream().limit(3).map(UserDto::from).toList();
    }
}
