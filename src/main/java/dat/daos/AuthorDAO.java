package dat.daos;


import dat.dtos.AuthorDTO;
import dat.entities.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Convert DTO to Entity
            Author author = new Author();
            author.setName(authorDTO.getName());
            author.setDescription(authorDTO.getDescription());

            // Persist the new Author entity
            em.persist(author);
            em.getTransaction().commit();

            // Return DTO of the persisted entity
            return new AuthorDTO(author);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public List<AuthorDTO> getAllAuthors() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Author> query = em.createQuery("FROM Author", Author.class);
            List<Author> authors = query.getResultList();

            // Convert List of Authors to List of AuthorDTOs
            return authors.stream()
                    .map(AuthorDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public Author findAuthorById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Author.class, id);
        }
    }

    public AuthorDTO updateAuthor(Integer id, AuthorDTO authorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Author found = em.find(Author.class, id);  // Fetch the Author entity
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

            return new AuthorDTO(found);  // Return the updated Author as a DTO
        }
    }


    public void deleteById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Author author = em.find(Author.class, id);
            if (author == null) {
                throw new EntityNotFoundException("No author found with ID: " + id);
            }
            em.remove(author);
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
