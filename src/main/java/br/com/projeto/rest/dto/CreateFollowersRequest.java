package br.com.projeto.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateFollowersRequest {
    @NotBlank(message = "followers is required")
    private Long followerId;

}
