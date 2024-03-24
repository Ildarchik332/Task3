package ru.Ildar.AstonREST.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ДТО доктора
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private String name;
    private String specialization;
    private List<PatientDTO> patientList;


    public DoctorDTO(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }
}
