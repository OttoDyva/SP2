package dat.daos;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.dtos.AuthorDTO;
import dat.entities.Author;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorDAOTest {

    private AuthorDAO authorDAO;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        authorDAO = AuthorDAO.getInstance(emf);
        Populate.populate(emf);
    }


    @AfterAll
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void create() {
        AuthorDTO newAuthorDTO = AuthorDTO.builder()
                .name("New Author")
                .description("Description of the new author")
                .build();

        AuthorDTO createdAuthorDTO = authorDAO.create(newAuthorDTO);
        AuthorDTO retrievedAuthorDTO = new AuthorDTO(authorDAO.findAuthorById(createdAuthorDTO.getId()));

        assertThat(retrievedAuthorDTO, is(notNullValue()));
        assertThat(retrievedAuthorDTO.getName(), is("New Author"));
    }

    @Test
    void getAllAuthors() {
        List<AuthorDTO> authorList = authorDAO.getAllAuthors();
        assertThat(authorList, is(not(empty())));
    }

    @Test
    void findAuthorById() {
        List<AuthorDTO> authorList = authorDAO.getAllAuthors();
        AuthorDTO firstAuthor = authorList.get(0);
        AuthorDTO foundAuthorDTO = new AuthorDTO(authorDAO.findAuthorById(firstAuthor.getId()));

        assertThat(foundAuthorDTO, is(notNullValue()));
        assertThat(foundAuthorDTO.getName(), is(firstAuthor.getName()));
    }

    @Test
    void testUpdateAuthor() {
        List<AuthorDTO> authorList = authorDAO.getAllAuthors();
        assertFalse(authorList.isEmpty(), "Author list should not be empty after population.");

        // Get the first author for testing
        AuthorDTO existingAuthorDTO = authorList.get(0);

        AuthorDTO updatedAuthorDTO = AuthorDTO.builder()
                .name("Updated Name")
                .description("Updated Description")
                .build();

        AuthorDTO updated = authorDAO.updateAuthor(existingAuthorDTO.getId(), updatedAuthorDTO);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }




    @Test
    void deleteById() {
        AuthorDTO newAuthorDTO = AuthorDTO.builder()
                .name("Author To Delete")
                .description("This author will be deleted")
                .build();

        AuthorDTO createdAuthorDTO = authorDAO.create(newAuthorDTO);
        int createdAuthorId = createdAuthorDTO.getId();

        authorDAO.deleteById(createdAuthorId);

        Author deletedAuthor = authorDAO.findAuthorById(createdAuthorId);
        assertThat(deletedAuthor, is(nullValue()));
    }

}
