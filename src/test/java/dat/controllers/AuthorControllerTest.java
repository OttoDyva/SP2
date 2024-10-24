package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.util.LoginUtil;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorControllerTest {

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
    public void testFindAuthorById() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/authors/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    public void testGetAllAuthors() {
       given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("api/authors/")
                .then()
                .statusCode(200)
                .body("size()", equalTo(5));
    }
    @Test
    void create() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .body("{\"name\":\"Author name\",\"description\":\"NY TEST JA TAK\"}")
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
                .body("{\"name\":\"Updated name\",\"description\":\"Updated description\"}")
                .when()
                .put("/api/authors/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated name"));
    }

    @Test
    void deleteByID() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .when()
                .delete("/api/authors/2")
                .then()
                .statusCode(204);
    }

    @Test
    void findAuthorByName() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/authors/name/P")
                .then()
                .statusCode(200)
                .body("name[0]", equalTo("Patrick Kjøller"));
    }

    @Test
    void findAuthorByDescription() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/authors/description/com")
                .then()
                .statusCode(200)
                .body("description[0]", equalTo("Comedian"));
    }
}