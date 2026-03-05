package br.com.projeto.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteFollowersRequest {
    @NotBlank(message = "followers is required")
    private Long followers;

    @NotBlank(message = "usuario is required")
    private Long usuario;

}
