package ru.Ildar.AstonREST.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO лекарства
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DrugDTO {
    private Long id;
    private String title;
    private List<PatientDTO> whoAccepts;
    private Long patientId;

    public DrugDTO(String title) {
        this.title = title;
    }

    public DrugDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
