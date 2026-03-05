package br.com.projeto.domain.repository;

import br.com.projeto.domain.model.FollowersEntity;
import br.com.projeto.domain.model.PostEntity;
import br.com.projeto.domain.model.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FollowersRepository implements PanacheRepository<FollowersEntity> {

    public boolean followersUser(UserEntity followers, UserEntity user) {
        var params = Parameters.with("followers", followers).and("usuario", user).map();
        PanacheQuery<FollowersEntity> query = find("followers =:followers and usuario =:usuario", params);
        Optional<FollowersEntity> followersEntity = query.firstResultOptional();
        return followersEntity.isPresent();
    }

    public List<FollowersEntity> findByUser(Long userId) {
        PanacheQuery<FollowersEntity> query = find("usuario.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUSer(Long followerID, Long userId) {
        var params = Parameters.with("userId", userId)
                .and("followerID", followerID).map();

        delete("followers.id =:followerID and usuario.id =:userId", params);
    }
}
