package dat.daos;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.dtos.AuthorDTO;
import dat.dtos.BarsDTO;
import dat.entities.Author;
import dat.entities.Bars;
import dat.entities.Genre;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BarsDAOTest {

    private BarsDAO barsDAO;
    private EntityManagerFactory emf;
    private AuthorDAO authorDAO;

    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactory("bars");
        barsDAO = new BarsDAO(emf);
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
    void create() {
        List<AuthorDTO> authors = authorDAO.getAllAuthors();
        assertFalse(authors.isEmpty(), "Author list should not be empty for creating a bar.");

        AuthorDTO authorDTO = authors.get(0);
        Author author = new Author(authorDTO);

        BarsDTO newBarDTO = new BarsDTO();
        newBarDTO.setTitle("New Bar Title");
        newBarDTO.setContent("Content of the new bar.");
        newBarDTO.setDate(LocalDate.of(2024, 10, 22));
        newBarDTO.setGenre(Genre.PHILOSOPHY);
        newBarDTO.setAuthorName(author.getName());
        newBarDTO.setAuthorDescription(author.getDescription());

        Bars newBar = new Bars(newBarDTO);
        newBar.setAuthor(author);

        barsDAO.create(newBar);
        Bars retrievedBar = barsDAO.findBarsById(newBar.getId());

        assertThat(retrievedBar, is(notNullValue()));
        assertThat(retrievedBar.getTitle(), is("New Bar Title"));
    }

    @Test
    void getAllBars() {
        List<Bars> barsList = barsDAO.getAllBars();
        assertThat(barsList, is(not(empty())));
    }

    @Test
    void findBarsById() {
        List<Bars> barsList = barsDAO.getAllBars();
        assertFalse(barsList.isEmpty(), "Bars list should not be empty for finding by ID.");

        Bars firstBar = barsList.get(0);
        Bars foundBar = barsDAO.findBarsById(firstBar.getId());

        assertThat(foundBar, is(notNullValue()));
        assertThat(foundBar.getTitle(), is(firstBar.getTitle()));
    }

    @Test
    void updateBars() {
        List<Bars> barsList = barsDAO.getAllBars();
        assertFalse(barsList.isEmpty(), "Bars list should not be empty after population.");

        Bars barToUpdate = barsList.get(0);
        barToUpdate.setTitle("Updated Title");

        Bars updatedBar = barsDAO.updateBars(barToUpdate);
        assertNotNull(updatedBar);
        assertEquals("Updated Title", updatedBar.getTitle());
    }

    @Test
    void deleteById() {
        Bars newBar = new Bars();
        newBar.setTitle("Bar to be deleted");
        newBar.setContent("Content of the bar to be deleted.");
        newBar.setDate(LocalDate.of(2024, 10, 23));
        newBar.setGenre(Genre.PHILOSOPHY);

        List<AuthorDTO> authors = authorDAO.getAllAuthors();
        assertFalse(authors.isEmpty(), "Author list should not be empty for creating a bar.");
        AuthorDTO authorDTO = authors.get(0);
        Author author = new Author(authorDTO);
        newBar.setAuthor(author);

        barsDAO.create(newBar);
        int createdBarId = newBar.getId();

        barsDAO.deleteById(createdBarId);

        Bars deletedBar = barsDAO.findBarsById(createdBarId);
        assertThat(deletedBar, is(nullValue()));
    }
}
