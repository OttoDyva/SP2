
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

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("bars");

        Set<Bars> barsForAuthor1 = getBarsForAuthor1();
        Set<Bars> barsForAuthor2 = getBarsForAuthor2();
        Set<Bars> barsForAuthor3 = getBarsForAuthor3();
        Set<Bars> barsForAuthor4 = getBarsForAuthor4();
        Set<Bars> barsForAuthor5 = getBarsForAuthor5();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Author author1 = new Author();
            author1.setName("David Bekhan");
            author1.setDescription("Athlete");
            author1.setBars(barsForAuthor1);

            Author author2 = new Author();
            author2.setName("Otto von Bismarck");
            author2.setDescription("Politician");
            author2.setBars(barsForAuthor2);

            Author author3 = new Author();
            author3.setName("Patrick Kjøller");
            author3.setDescription("Comedian");
            author3.setBars(barsForAuthor3);

            Author author4 = new Author();
            author4.setName("Nelson Mandela");
            author4.setDescription("President");
            author4.setBars(barsForAuthor4);

            Author author5 = new Author();
            author5.setName("Steve Jobs");
            author5.setDescription("Businessman");

            barsForAuthor1.forEach(bar -> bar.setAuthor(author1));
            barsForAuthor2.forEach(bar -> bar.setAuthor(author2));
            barsForAuthor3.forEach(bar -> bar.setAuthor(author3));
            barsForAuthor4.forEach(bar -> bar.setAuthor(author4));
            barsForAuthor5.forEach(bar -> bar.setAuthor(author5));

            em.persist(author1);
            em.persist(author2);
            em.persist(author3);
            em.persist(author4);
            em.persist(author5);

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

    @NotNull
    private static Set<Bars> getBarsForAuthor3() {
        Bars bar1 = new Bars();
        bar1.setTitle("The Joke of the Day");
        bar1.setContent("Life is like a box of chocolates. It doesn’t last long if you’re fat");
        bar1.setDate(LocalDate.of(2024, 2, 15));
        bar1.setGenre(Genre.HUMOR);

        Bars bar2 = new Bars();
        bar2.setTitle("The Comedian's Life");
        bar2.setContent("The first time I met my wife, I knew she was a keeper. She was wearing massive gloves.");
        bar2.setDate(LocalDate.of(2024, 3, 1));
        bar2.setGenre(Genre.HUMOR);

        Bars[] barsArray = {bar1, bar2};
        return Set.of(barsArray);
    }

    @NotNull
    private static Set<Bars> getBarsForAuthor4() {
        Bars bar1 = new Bars();
        bar1.setTitle("The Struggle for Freedom");
        bar1.setContent("The greatest glory in living lies not in never falling, but in rising every time we fall.");
        bar1.setDate(LocalDate.of(2024, 6, 18));
        bar1.setGenre(Genre.PHILOSOPHY);

        Bars bar2 = new Bars();
        bar2.setTitle("The Road to Democracy");
        bar2.setContent("Democracy is the road to freedom...");
        bar2.setDate(LocalDate.of(2024, 7, 1));
        bar2.setGenre(Genre.PHILOSOPHY);

        Bars[] barsArray = {bar1, bar2};
        return Set.of(barsArray);
    }

    @NotNull
    private static Set<Bars> getBarsForAuthor5() {
        Bars bar1 = new Bars();
        bar1.setTitle("Life lesson");
        bar1.setContent("Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking");
        bar1.setDate(LocalDate.of(2024, 8, 15));
        bar1.setGenre(Genre.MOTIVATIONAL);

        Bars[] barsArray = {bar1};
        return Set.of(barsArray);
    }
}



