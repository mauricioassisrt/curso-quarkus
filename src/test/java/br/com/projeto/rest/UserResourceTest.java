package br.com.projeto.rest;

import br.com.projeto.rest.dto.CreateUserRequest;
import br.com.projeto.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {
    @TestHTTPResource("/users")
    URL apiUrl;

    @Test
    @DisplayName("Criar usuario com sucesso!")
    @Order(1)
    public void createUserTest() {
        var userRequest = new CreateUserRequest();
        userRequest.setName("Nome");
        userRequest.setAge(10);

        var response = given().
                contentType(ContentType.JSON).body(userRequest).when().post(apiUrl).then().extract().response();
        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString(""));
    }

    @Test
    @Order(2)
    public void createUserValidation() {
        var userRequest = new CreateUserRequest();
        userRequest.setName(null);
        userRequest.setAge(null);

        var response = given().contentType(ContentType.JSON).body(userRequest).when().post(apiUrl).then().extract().response();
        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.getStatusCode());
        assertEquals("Validation errors", response.jsonPath().getString("message"));
        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        //assertEquals("Name is required", errors.get(0).get("message"));
       // assertEquals("Age is required", errors.get(1).get("message"));
    }

    @Test
    @Order(3)
    public void getAllUsersTest() {
        var response = given().
                contentType(ContentType.JSON).then().statusCode(200).body("size()", Matchers.is(1))
                .when().get(apiUrl);
    }
}