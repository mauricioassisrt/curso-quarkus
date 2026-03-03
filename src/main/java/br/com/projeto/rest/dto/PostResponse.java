package br.com.projeto.rest.dto;

import br.com.projeto.domain.model.PostEntity;
import lombok.Data;

@Data
public class PostResponse {
    private String description;

    public static PostResponse fromToEntity(PostEntity post) {
        var response = new PostResponse();
        response.setDescription(post.getDescription());
        return response;
    }
}
