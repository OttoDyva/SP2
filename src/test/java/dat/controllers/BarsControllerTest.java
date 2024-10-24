package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.util.LoginUtil;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class BarsControllerTest {

    private Javalin app;
    EntityManagerFactory emfTest;
    private int port = 9090;
    private static String adminToken;
    private static String userToken;

    @BeforeEach
    void setUp() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(port);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        LoginUtil.createTestUsers(emfTest);
        adminToken = LoginUtil.getAdminToken();
        userToken = LoginUtil.getUserToken();
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
                .get("/api/bars/9")
                .then()
                .statusCode(200)
                .body("id", equalTo(9));
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
    void testDeleteBar() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete("/api/bars/11")
                .then()
                .statusCode(204);
    }
}
