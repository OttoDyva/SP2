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
import org.junit.jupiter.api.Test;
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

    @AfterAll
    void afterAll() {
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
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{ \"title\": \"Updated Bar\",\"content\":\"Content of the bar\",\"genre\":\"PHILOSOPHY\",\"date\":\"2024-10-01\",\"authorName\":\"bob marley\",\"authorDescription\":\"fuck dig\"}")
                .when()
                .put("/api/bars/2")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Bar"));
    }

    @Test
    void testDeleteBar() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete("/api/bars/5")
                .then()
                .statusCode(204);
    }
}
