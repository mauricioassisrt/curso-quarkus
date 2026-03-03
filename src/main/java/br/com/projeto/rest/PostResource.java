package br.com.projeto.rest;

import br.com.projeto.domain.model.PostEntity;
import br.com.projeto.domain.model.UserEntity;
import br.com.projeto.domain.repository.PostRepository;
import br.com.projeto.domain.repository.UserRepository;
import br.com.projeto.rest.dto.CreatePostUserRequest;
import br.com.projeto.rest.dto.PostResponse;
import br.com.projeto.rest.dto.ResponseError;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private final Validator validator;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Inject
    public PostResource(Validator validator, UserRepository userRepository, PostRepository postRepository1) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.postRepository = postRepository1;
    }

    @POST
    @Transactional
    public Response createPostToUser(@PathParam("userId") Long id, CreatePostUserRequest createPostUserRequest) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Set<ConstraintViolation<CreatePostUserRequest>> validations = validator.validate(createPostUserRequest);

        if (!validations.isEmpty()) {
            return ResponseError.createFromValidation(validations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        PostEntity post = new PostEntity();
        post.setUsuario(user);
        post.setDescription(createPostUserRequest.getDescription());
        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var query = postRepository.find("usuario", user);
        var list = query.list();
        var postResponse = list.stream().map(
                PostResponse::fromToEntity
        ).collect(Collectors.toList());
        return Response.ok(postResponse).build();
    }
}
