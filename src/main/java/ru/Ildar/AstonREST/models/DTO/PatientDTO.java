package ru.Ildar.AstonREST.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO пациента
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private Integer age;
    private String sex;
    private Long doctorId;

    private List<DrugDTO> drugList;

    public PatientDTO(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public PatientDTO(String name, Integer age, String sex, Long doctorId) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.doctorId = doctorId;
    }

    public PatientDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public PatientDTO(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
