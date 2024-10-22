package dat.daos;


import dat.entities.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AuthorDAO {
    private EntityManagerFactory emf;

    public AuthorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Author author) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
        }
    }

    public List<Author> getAllAuthors() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Author> query = em.createQuery("FROM Author", Author.class);
            return query.getResultList();
        }
    }

    public Author findAuthorById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Author.class, id);
        }
    }

    public Author updateAuthor(Author author) {
        try (EntityManager em = emf.createEntityManager()) {
            Author found = em.find(Author.class, author.getId());
            if (found == null) {
                throw new EntityNotFoundException("No author found with that ID");
            }

            em.getTransaction().begin();
            if (author.getName() != null) {
                found.setName(author.getName());
            }
            if (author.getDescription() != null) {
                found.setDescription(author.getDescription());
            }
            em.getTransaction().commit();
            return found;
        }
    }

    public void deleteById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Author author = findAuthorById(id);
            if (author != null) {
                em.remove(author);
            }
            em.getTransaction().commit();
        }
    }
}
