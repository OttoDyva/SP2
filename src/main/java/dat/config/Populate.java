
package dat.config;

import dat.entities.Author;
import dat.entities.Bars;
import dat.entities.Genre;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Bars> barsForAuthor1 = getBarsForAuthor1();
        Set<Bars> barsForAuthor2 = getBarsForAuthor2();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Author author1 = new Author();
            author1.setName("David Bekhan");
            author1.setDescription("Famous for philosophy writings");
            author1.setBars(barsForAuthor1);

            Author author2 = new Author();
            author2.setName("Otto von Bismarck");
            author2.setDescription("Known for motivational speeches");
            author2.setBars(barsForAuthor2);

            barsForAuthor1.forEach(bar -> bar.setAuthor(author1));
            barsForAuthor2.forEach(bar -> bar.setAuthor(author2));

            em.persist(author1);
            em.persist(author2);

            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Bars> getBarsForAuthor1() {
        Bars bar1 = new Bars();
        bar1.setTitle("Philosophy in a Nutshell");
        bar1.setContent("Philosophy is the study of existence...");
        bar1.setDate(LocalDate.of(2023, 5, 14));
        bar1.setGenre(Genre.PHILOSOPHY);

        Bars bar2 = new Bars();
        bar2.setTitle("The Poem of the Moon");
        bar2.setContent("Oh moon, so bright and high...");
        bar2.setDate(LocalDate.of(2022, 3, 21));
        bar2.setGenre(Genre.POEM);

        Bars[] barsArray = {bar1, bar2};
        return Set.of(barsArray);
    }

    @NotNull
    private static Set<Bars> getBarsForAuthor2() {
        Bars bar1 = new Bars();
        bar1.setTitle("Motivational Speech 101");
        bar1.setContent("Success is not final, failure is not fatal...");
        bar1.setDate(LocalDate.of(2024, 1, 10));
        bar1.setGenre(Genre.MOTIVATIONAL);

        Bars bar2 = new Bars();
        bar2.setTitle("Humor in the Modern Age");
        bar2.setContent("Why did the chicken cross the road?");
        bar2.setDate(LocalDate.of(2024, 4, 1));
        bar2.setGenre(Genre.HUMOR);

        Bars[] barsArray = {bar1, bar2};
        return Set.of(barsArray);
    }
}



