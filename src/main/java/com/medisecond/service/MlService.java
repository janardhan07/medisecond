package com.medisecond.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple keyword-based ML service that predicts the appropriate doctor specialty
 * based on patient-reported symptoms. Mirrors the Python sklearn MVP logic.
 */
@Service
public class MlService {

    private static final Map<String, String> KEYWORD_SPECIALTY = new LinkedHashMap<>();

    static {
        // Cardiology
        KEYWORD_SPECIALTY.put("chest pain", "Cardiologist");
        KEYWORD_SPECIALTY.put("heart", "Cardiologist");
        KEYWORD_SPECIALTY.put("palpitation", "Cardiologist");
        KEYWORD_SPECIALTY.put("shortness of breath", "Cardiologist");

        // Neurology
        KEYWORD_SPECIALTY.put("headache", "Neurologist");
        KEYWORD_SPECIALTY.put("migraine", "Neurologist");
        KEYWORD_SPECIALTY.put("dizziness", "Neurologist");
        KEYWORD_SPECIALTY.put("blurred vision", "Neurologist");
        KEYWORD_SPECIALTY.put("seizure", "Neurologist");
        KEYWORD_SPECIALTY.put("numbness", "Neurologist");

        // Gastroenterology
        KEYWORD_SPECIALTY.put("stomach", "Gastroenterologist");
        KEYWORD_SPECIALTY.put("nausea", "Gastroenterologist");
        KEYWORD_SPECIALTY.put("vomiting", "Gastroenterologist");
        KEYWORD_SPECIALTY.put("diarrhea", "Gastroenterologist");
        KEYWORD_SPECIALTY.put("constipation", "Gastroenterologist");
        KEYWORD_SPECIALTY.put("abdominal", "Gastroenterologist");

        // Dermatology
        KEYWORD_SPECIALTY.put("skin rash", "Dermatologist");
        KEYWORD_SPECIALTY.put("itching", "Dermatologist");
        KEYWORD_SPECIALTY.put("acne", "Dermatologist");
        KEYWORD_SPECIALTY.put("eczema", "Dermatologist");

        // General Physician (catch-all)
        KEYWORD_SPECIALTY.put("fever", "General Physician");
        KEYWORD_SPECIALTY.put("cough", "General Physician");
        KEYWORD_SPECIALTY.put("cold", "General Physician");
        KEYWORD_SPECIALTY.put("fatigue", "General Physician");
        KEYWORD_SPECIALTY.put("weakness", "General Physician");
    }

    public record Prediction(String specialty, double confidence) {
    }

    public Prediction predict(String symptoms) {
        if (symptoms == null || symptoms.isBlank()) {
            return new Prediction("General Physician", 0.5);
        }

        String lower = symptoms.toLowerCase();
        Map<String, Integer> scores = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : KEYWORD_SPECIALTY.entrySet()) {
            if (lower.contains(entry.getKey())) {
                scores.merge(entry.getValue(), 1, Integer::sum);
            }
        }

        if (scores.isEmpty()) {
            return new Prediction("General Physician", 0.4);
        }

        String topSpecialty = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("General Physician");

        int totalMatches = scores.values().stream().mapToInt(i -> i).sum();
        int topMatches = scores.get(topSpecialty);
        double confidence = Math.min(0.95, 0.5 + (double) topMatches / (totalMatches + 2));

        return new Prediction(topSpecialty, Math.round(confidence * 100.0) / 100.0);
    }
}
