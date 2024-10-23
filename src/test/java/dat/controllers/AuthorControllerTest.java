package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import dat.entities.Author;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

class AuthorControllerTest {

    private static EntityManagerFactory emf;
    private AuthorDAO authorDAO;
    private static Context ctx;
    private AuthorController authorController;


    @BeforeAll
    public static void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();

    }

    @AfterAll
    public static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testFindAuthorById() {

    }

    @Test
    public void testGetAllAuthors() {

    }

    @Test
    void create() {
    }

    @Test
    void updateAuthor() {
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