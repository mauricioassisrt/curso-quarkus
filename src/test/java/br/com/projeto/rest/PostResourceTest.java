package br.com.projeto.rest;

import br.com.projeto.domain.model.UserEntity;
import br.com.projeto.domain.repository.UserRepository;
import br.com.projeto.rest.dto.CreatePostUserRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository repository;
    Long id;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new UserEntity();
        user.setName("NOme");
        user.setAge(1);
        repository.persist(user);
        id = user.getId();
    }

    @Test
    @DisplayName("Criar um post para um usuario")
    public void createPostTest() {
        var userID = id;
        var postRequest = new CreatePostUserRequest();
        postRequest.setDescription("Descricao");
        given().contentType(ContentType.JSON).body(postRequest).pathParam("userId", userID).when().post().then().statusCode(201);
    }

    @Test
    @DisplayName("Nao encontra o usuario para um posts")
    public void inexistenteUserTest() {
        var inexistente = 5;
        var postRequest = new CreatePostUserRequest();
        postRequest.setDescription("Descricao");
        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistente)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    public void listPostNotFoundTest() {
        var userId = 999;
        given().pathParam("userId", userId)
                .when().get().then().statusCode(404);
    }

    @Test
    public void listPostFollowerHeaderNotSendTest() {

        given().pathParam("userId", id)
                .when().get().then().statusCode(400).body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    public void listPostNotAFollowerTest() {
        var inexistente = 999;
        given().pathParam("userId", id).header("followerId", inexistente)
                .when().get().then().statusCode(400).body(Matchers.is("You forgot the header followerId"));
    }


    @Test
    public void listPostTest() {

    }
}