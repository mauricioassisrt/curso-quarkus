package br.com.projeto.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostUserRequest {
    @NotBlank(message = "description is required")
    private String description;

}
