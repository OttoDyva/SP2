package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.util.LoginUtil;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorControllerTest {

    private Javalin app;

    private int port = 9090;
    private static String adminToken;
    private static String userToken;

    @BeforeAll
    void setup() {
        EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(port);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        LoginUtil.createTestUsers(emfTest);
        adminToken = LoginUtil.getAdminToken();
        userToken = LoginUtil.getUserToken();

    }

    @AfterAll
    void tearDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    public void testFindAuthorById() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType("application/json")
                .when()
                .get("/api/authors/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    public void testGetAllAuthors() {

    }

    @Test
    void create() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType("application/json")
                .body("{\"name\":\"Author name\",\"description\":\"TEST\",\"bars\":\"TEST\"}")
                .when()
                .post("/api/authors/")
                .then()
                .statusCode(201)
                .body("name", equalTo("Author name"));

    }

    @Test
    void updateAuthor() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{\"name\":\"Updated name\",\"description\":\"Updated description\",\"bars\":\"Updated bar\"}")
                .when()
                .put(baseURI+"/api/authors/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated name"));
    }

    @Test
    void deleteByID() {
    }

    @Test
    void validatePrimaryKey() {
    }

    @Test
    void validateEntity() {
    }
}