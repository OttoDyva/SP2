package dat.daos;

import dat.entities.Author;
import dat.entities.Bars;
import dat.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class BarsDAO {
    private EntityManagerFactory emf;

    public BarsDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Bars bars) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Check if an author with the given name exists
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE LOWER(a.name) = LOWER(:name)", Author.class);
            query.setParameter("name", bars.getAuthor().getName());
            List<Author> existingAuthors = query.getResultList();

            Author author;
            if (!existingAuthors.isEmpty()) {
                // Use the existing author
                author = existingAuthors.get(0);
            } else {
                // Create a new author if not found
                author = bars.getAuthor();
                em.persist(author);
            }

            // Associate the bar with the author
            bars.setAuthor(author);
            em.persist(bars);

            em.getTransaction().commit();
        }
    }

    public List<Bars> getAllBars() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Bars> query = em.createQuery("FROM Bars", Bars.class);
            System.out.println(query);
            return query.getResultList();
        }
    }

    public Bars findBarsById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Bars.class, id);
        }
    }

    public List<Bars> findBarsByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Bars> query = em.createQuery("SELECT b FROM Bars b WHERE LOWER(b.title) LIKE LOWER(:title)", Bars.class);
            query.setParameter("title", "%" + title + "%");
            return query.getResultList();
        }
    }

    public List<Bars> findBarsByGenre(String genre) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Bars> query = em.createQuery("SELECT b FROM Bars b WHERE LOWER(b.genre) LIKE LOWER(:genre)", Bars.class);
            query.setParameter("genre", "%" + genre + "%");
            return query.getResultList();
        }
    }

    public Bars updateBars(Integer integer, Bars bars) {
        try (EntityManager em = emf.createEntityManager()) {
            Bars found = em.find(Bars.class, bars.getId());
            if (found == null) {
                throw new EntityNotFoundException("No bars found with that ID");
            }

            em.getTransaction().begin();
            if (bars.getTitle() != null) {
                found.setTitle(bars.getTitle());
            }
            if (bars.getContent() != null) {
                found.setContent(bars.getContent());
            }
            if (bars.getDate() != null) {
                found.setDate(bars.getDate());
            }
            if (bars.getGenre() != null) {
                found.setGenre(bars.getGenre());
            }
            em.getTransaction().commit();
            return found;
        }
    }

    public void deleteById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Bars bars = findBarsById(integer);
            if (bars != null) {
                em.remove(bars);
            }
            em.getTransaction().commit();
        }
    }
}
