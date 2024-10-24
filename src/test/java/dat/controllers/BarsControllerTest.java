package dat.controllers;

import dat.config.ApplicationConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class BarsControllerTest {

    private Javalin app;
    private int port = 9090;

    @BeforeEach
    void setUp() {
        app = ApplicationConfig.startServer(port);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testGetAllBars() {
        Response response = given()
                .when()
                .get("/api/bars/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().jsonPath().getList("$").size() >= 0);
    }

    @Test
    void testGetBarsById() {
        given()
                .when()
                .get("/api/bars/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void testCreateBar() {
        given()
                .contentType("application/json")
                .body("{ \"title\": \"New Bar\", \"content\": \"Content of the bar\", \"genre\": \"PHILOSOPHY\", \"date\": \"2024-10-01\" }")
                .when()
                .post("/api/bars/")
                .then()
                .statusCode(201)
                .body("title", equalTo("New Bar"));
    }

    @Test
    void testUpdateBar() {
        given()
                .contentType("application/json")
                .body("{ \"title\": \"Updated Bar\" }")
                .when()
                .put("/api/bars/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Bar"));
    }

    @Test
    void testDeleteBar() {
        given()
                .when()
                .delete("/api/bars/1")
                .then()
                .statusCode(204);
    }

    @Test
    void testHandleException() {
        given()
                .when()
                .get("/api/bars/999")
                .then()
                .statusCode(404);
    }
}
