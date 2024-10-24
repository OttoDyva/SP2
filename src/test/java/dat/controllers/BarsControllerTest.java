package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.util.LoginUtil;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BarsControllerTest {

    private Javalin app;
    private static EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private int port = 9090;
    private static String adminToken;
    private static String userToken;

    @BeforeAll
    void setup() {
        app = ApplicationConfig.startServer(port);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        LoginUtil.createTestUsers(emfTest);
        adminToken = LoginUtil.getAdminToken();
        userToken = LoginUtil.getUserToken();
        Populate.populate(emfTest);
    }

    @AfterEach
    void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testGetAllBars() {
        Response response = given()
                .header("Authorization", adminToken)
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
                .header("Authorization", adminToken)
                .when()
                .get("/api/bars/3")
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
    }

    @Test
    void testCreateBar() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{ \"title\": \"New Bar\",\"content\":\"Content of the bar\",\"genre\":\"PHILOSOPHY\",\"date\":\"2024-10-01\",\"authorName\":\"bob marley\",\"authorDescription\":\"fuck mig\"}")
                .when()
                .post("/api/bars/")
                .then()
                .statusCode(201)
                .body("title", equalTo("New Bar"));
    }

    @Test
    void testUpdateBar() {
        String updatedBarJson = "{ \"id\": 1, \"title\": \"Updated Bar\",\"content\":\"Updated Content\",\"genre\":\"PHILOSOPHY\",\"date\":\"2024-10-01\",\"authorName\":\"bob marley\",\"authorDescription\":\"Updated description\"}";

        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(updatedBarJson)
                .when()
                .put("/api/bars/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Bar"));
    }

    @Test
    void getBarsByGenre() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/bars/genre/P")
                .then()
                .statusCode(200)
                .body("genre[0]", CoreMatchers.equalTo("PHILOSOPHY"));
    }

    @Test
    void getBarsByTitle() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/bars/title/M")
                .then()
                .statusCode(200)
                .body("title[0]", CoreMatchers.equalTo("The Poem of the Moon"));
    }

    @Test
    void testDeleteBar() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete("/api/bars/11")
                .then()
                .statusCode(204);
    }
}
