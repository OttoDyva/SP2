package dat.controllers;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import dat.entities.Author;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorControllerTest {

    private EntityManagerFactory emf;
    private AuthorDAO authorDAO;
    private AuthorController authorController;

    private Context ctx;


    @BeforeAll
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        authorDAO = AuthorDAO.getInstance(emf);
        Populate.main(null);

    }

    @AfterAll
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testFindAuthorById() {
        /*
        ctx.pathParam("1");

        AuthorController authorController = new AuthorController();

        authorController.findAuthorById(ctx);

        assertEquals(200, ctx.res().getStatus());
        AuthorDTO authorDTO = (AuthorDTO) ctx.json(AuthorDTO.class);
        assertNotNull(authorDTO);
        assertEquals(1, authorDTO.getId());

         */

        /*
        AuthorDTO request = AuthorDTO.builder()
                .id(10)
                .name("Author name")
                .description("Author description")
                .build();
        AuthorDTO response = AuthorDTO.builder()
                .id(11)
                .name("Author name")
                .description("Author description")
                .build();

        when(ctx.pathParamAsClass("id", Integer.class)).thenReturn(1);
        when(authorDAO.findAuthorById(1)).thenReturn(response);
        when(ctx.bodyValidator(AuthorDTO.class)).thenReturn(Validator.create(request));
        when(authorDAO.create(request)).thenReturn(response);

        authorController.create(ctx);

        verify(ctx).status(201);  // Status 201 for created
        verify(ctx).json(response);  // JSON response with created author

         */

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