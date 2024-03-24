package ru.Ildar.AstonREST.models.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность Доктор
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "specialization"})
public class Doctor {
    private Long id;
    private String name;
    private String specialization;
    private List<Patient> patientList; // сделать не айди пациентов кому назначены, а их имена.

    public Doctor(Long id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }
}
