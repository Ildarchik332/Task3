package ru.Ildar.AstonREST.models.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность пациент
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "age", "sex"})
public class Patient {
    private Long id;
    private String name;
    private Integer age;
    private String sex;

    private Long doctorId;

    private String titleDrug;

    private List<Drug> drugList;


    public Patient(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Patient(String name, Integer age, String sex, Long doctorId) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.doctorId = doctorId;
    }
}
