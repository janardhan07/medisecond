package com.medisecond.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Long doctorId;
    private String appointmentDate, appointmentTime, symptoms, notes, consultType;
}
