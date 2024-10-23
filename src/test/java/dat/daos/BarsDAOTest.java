package dat.daos;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.entities.Bars;
import dat.entities.Genre;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BarsDAOTest {

    private BarsDAO barsDAO;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        barsDAO = new BarsDAO(emf);
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
        Bars newBar = new Bars();
        newBar.setTitle("New Bar Title");
        newBar.setContent("Content of the new bar.");
        newBar.setDate(LocalDate.of(2024, 10, 22));
        newBar.setGenre(Genre.PHILOSOPHY);

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
        Bars firstBar = barsList.get(0);
        Bars foundBar = barsDAO.findBarsById(firstBar.getId());

        assertThat(foundBar, is(notNullValue()));
        assertThat(foundBar.getTitle(), is(firstBar.getTitle()));
    }

    @Test
    void updateBars() {
        List<Bars> barsList = barsDAO.getAllBars();
        Bars barToUpdate = barsList.get(0);
        barToUpdate.setTitle("Updated Title");

        Bars updatedBar = barsDAO.updateBars(barToUpdate);
        assertThat(updatedBar.getTitle(), is("Updated Title"));
    }

    @Test
    void deleteById() {
        Bars newBar = new Bars();
        newBar.setTitle("Bar to be deleted");
        newBar.setContent("Content of the bar to be deleted.");
        newBar.setDate(LocalDate.of(2024, 10, 23));
        newBar.setGenre(Genre.PHILOSOPHY);

        barsDAO.create(newBar);
        barsDAO.deleteById(newBar.getId());

        Bars deletedBar = barsDAO.findBarsById(newBar.getId());
        assertThat(deletedBar, is(nullValue()));
    }
}
