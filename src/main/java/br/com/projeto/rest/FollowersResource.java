package br.com.projeto.rest;

import br.com.projeto.domain.model.FollowersEntity;
import br.com.projeto.domain.model.PostEntity;
import br.com.projeto.domain.model.UserEntity;
import br.com.projeto.domain.repository.FollowersRepository;
import br.com.projeto.domain.repository.PostRepository;
import br.com.projeto.domain.repository.UserRepository;
import br.com.projeto.rest.dto.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowersResource {
    private final Validator validator;
    private final UserRepository userRepository;
    private final FollowersRepository followersRepository;

    @Inject
    public FollowersResource(Validator validator, UserRepository userRepository, FollowersRepository followersRepository) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.followersRepository = followersRepository;
    }

    @GET
    public Response getFollowersByUser(@PathParam("userId") Long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var users = followersRepository.findByUser(id);
        FollowersPerUserResponse response = new FollowersPerUserResponse();
        response.setFollowersCount(users.size());
        var followers = users.stream().map(FollowersResponse::new)
                .collect(Collectors.toList());
        response.setContent(followers);
        return Response.ok(response).build();
    }


    @PUT
    @Transactional
    public Response updateFollowers(@PathParam("userId") Long id,
                                    CreateFollowersRequest createFollowersRequest
    ) {
        if (id.equals(createFollowersRequest.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).entity("You can't follow your self").build();
        }
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var follower = userRepository.findById(createFollowersRequest.getFollowerId());
        boolean followers = followersRepository.followersUser(follower, user);
        if (!followers) {
            var followersEntity = new FollowersEntity();
            followersEntity.setFollowers(follower);
            followersEntity.setUsuario(user);
            followersRepository.persist(followersEntity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Transactional
    public Response unfollowerUser(@PathParam("userId") Long id, @QueryParam("followersId") Long followerID) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        followersRepository.deleteByFollowerAndUSer(followerID, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
