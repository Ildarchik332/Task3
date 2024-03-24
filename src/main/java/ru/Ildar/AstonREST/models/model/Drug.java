package ru.Ildar.AstonREST.models.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность лекарство
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drug {
    private Long id;
    private String title;

    private Long patientId;

    private List<Patient> whoAccepts;
}
