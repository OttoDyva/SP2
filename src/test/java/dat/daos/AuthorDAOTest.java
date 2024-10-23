/*
package dat.daos;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.entities.Author;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorDAOTest {

    private AuthorDAO authorDAO;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        authorDAO = new AuthorDAO(emf);
        Populate.main(null);
    }

    @AfterAll
    void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void create() {
        Author newAuthor = new Author();
        newAuthor.setName("newName");
        newAuthor.setDescription("bingbong");

        authorDAO.create(newAuthor);
        Author retrievedAuthor = authorDAO.findAuthorById(newAuthor.getId());

        assertThat(retrievedAuthor, is(notNullValue()));
        assertThat(retrievedAuthor.getName(), is("newName"));
    }

    @Test
    void getAllAuthors() {
        List<Author> authorList = authorDAO.getAllAuthors();
        assertThat(authorList, is(not(empty())));
    }

    @Test
    void findAuthorById() {
        List<Author> authorList = authorDAO.getAllAuthors();
        Author firstAuthor = authorList.get(0);
        Author foundAuthor = authorDAO.findAuthorById(firstAuthor.getId());

        assertThat(foundAuthor, is(notNullValue()));
        assertThat(foundAuthor.getName(), is(firstAuthor.getName()));
    }

    @Test
    void updateAuthor() {
        List<Author> authorList = authorDAO.getAllAuthors();
        Author authorToUpdate = authorList.get(0);
        authorToUpdate.setName("newName");

        Author updatedAuthor = authorDAO.updateAuthor(authorToUpdate);
        assertThat(updatedAuthor.getName(), is("newName"));
    }

    @Test
    void deleteById() {
        Author newAuthor = new Author();
        newAuthor.setName("newAuthor");
        newAuthor.setDescription("kill");

        authorDAO.create(newAuthor);
        authorDAO.deleteById(newAuthor.getId());

        Author deletedAuthor = authorDAO.findAuthorById(newAuthor.getId());
        assertThat(deletedAuthor, is(nullValue()));
    }
}
*/
