package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import dat.entities.Author;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class AuthorController {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("bars");
    private AuthorDAO authorDAO = new AuthorDAO(emf);

    public AuthorController() {
        this.authorDAO = AuthorDAO.getInstance(emf);
    }

    public void findAuthorById(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        Author author = authorDAO.findAuthorById(id);
        AuthorDTO authorDTO = new AuthorDTO(author);
        // response
        ctx.res().setStatus(200);
        ctx.json(authorDTO, AuthorDTO.class);
    }

    public void getAllAuthors(Context ctx) {
        try {
            // Fetch all authors from the DAO
            List<AuthorDTO> authorList = authorDAO.getAllAuthors();

            // Respond with the list of authors
            ctx.status(200).json(authorList);
        } catch (Exception e) {
            ctx.status(500).result("Error fetching authors: " + e.getMessage());
            e.printStackTrace();  // Log the error for debugging
        }
    }

    public void create(Context ctx) {
        try {
            // Validate the request body and map it to AuthorDTO
            AuthorDTO jsonRequest = ctx.bodyValidator(AuthorDTO.class)
                    .check(dto -> dto.getName() != null && !dto.getName().isEmpty(), "Author name must be set")
                    .check(dto -> dto.getDescription() != null && !dto.getDescription().isEmpty(), "Author description must be set")
                    .get();

            // Create the author using DAO
            AuthorDTO authorDTO = authorDAO.create(jsonRequest);

            // Respond with the created author
            ctx.status(201).json(authorDTO);
        } catch (Exception e) {
            ctx.status(500).result("Error creating author: " + e.getMessage());  // Respond with a 500 error and message
            e.printStackTrace();  // Log the stack trace for debugging
        }
    }


    public void updateAuthor(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        AuthorDTO updatedAuthor = authorDAO.updateAuthor(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(updatedAuthor);
    }

    public void deleteByID(Context ctx) {
        try {
            // Extract the author ID from the path parameter
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid ID").get();

            // Call the DAO to delete the author
            authorDAO.deleteById(id);

            // Respond with 204 No Content (success)
            ctx.status(204);
        } catch (EntityNotFoundException e) {
            // Handle the case where the author is not found
            ctx.status(404).result("Author not found: " + e.getMessage());
        } catch (Exception e) {
            // Handle other potential exceptions
            ctx.status(500).result("Error deleting author: " + e.getMessage());
        }
    }

    public boolean validatePrimaryKey(Integer integer) {
        return authorDAO.validatePrimaryKey(integer);
    }

    public AuthorDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(AuthorDTO.class)
                .check( a -> a.getName() != null && !a.getName().isEmpty(), "Author name must be set")
                .check( a -> a.getDescription() != null && !a.getDescription().isEmpty(), "Author description must be set")
                .get();
    }

}
