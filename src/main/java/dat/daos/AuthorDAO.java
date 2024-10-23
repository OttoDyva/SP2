package dat.daos;


import dat.dtos.AuthorDTO;
import dat.entities.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AuthorDAO {
    private static EntityManagerFactory emf;

    private static AuthorDAO instance;

    public AuthorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static AuthorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AuthorDAO(emf);
        }
        return instance;
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Author author = new Author(authorDTO);
            em.persist(author);
            em.getTransaction().commit();
            return new AuthorDTO(author);
        }
    }

    public List<Author> getAllAuthors() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Author> query = em.createQuery("FROM Author", Author.class);
            return query.getResultList();
        }
    }

    public AuthorDTO findAuthorById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(AuthorDTO.class, id);
        }
    }

    public AuthorDTO updateAuthor(Integer integer, AuthorDTO authorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            AuthorDTO found = em.find(AuthorDTO.class, authorDTO.getId());
            if (found == null) {
                throw new EntityNotFoundException("No author found with that ID");
            }

            em.getTransaction().begin();
            if (authorDTO.getName() != null) {
                found.setName(authorDTO.getName());
            }
            if (authorDTO.getDescription() != null) {
                found.setDescription(authorDTO.getDescription());
            }
            em.getTransaction().commit();
            return found;
        }
    }

    public void deleteById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Author author = em.find(Author.class, integer);
            if (author != null) {
                em.remove(author);
            }
            em.getTransaction().commit();
        }
    }

    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Author author = em.find(Author.class, integer);
            return author != null;
        }
    }
}
