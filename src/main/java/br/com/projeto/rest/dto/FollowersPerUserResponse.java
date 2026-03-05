package br.com.projeto.rest.dto;

import br.com.projeto.domain.model.FollowersEntity;
import lombok.Data;

import java.util.List;

@Data
public class FollowersPerUserResponse {
    private Integer followersCount;
    private List<FollowersResponse> content;

}
