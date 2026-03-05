package br.com.projeto.rest.dto;

import br.com.projeto.domain.model.FollowersEntity;
import br.com.projeto.domain.model.PostEntity;
import lombok.Data;

@Data
public class FollowersResponse {
    private Long id;
    private String name;

    public FollowersResponse() {

    }

    public FollowersResponse(FollowersEntity followersEntity) {
        this(followersEntity.getId(), followersEntity.getFollowers().getName());
    }

    public FollowersResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
