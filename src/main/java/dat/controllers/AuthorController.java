package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import dat.entities.Author;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

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

    /*
    public void getAllAuthors(Context ctx) {
        // List of DTOS
        List<AuthorDTO> hotelDTOS = authorDAO.getAllAuthors();
        // response
        ctx.res().setStatus(200);
        ctx.json(hotelDTOS, HotelDTO.class);
    }

     */

    public void create(Context ctx) {
        // request
        AuthorDTO jsonRequest = ctx.bodyAsClass(AuthorDTO.class);
        // DTO
        AuthorDTO authorDTO = authorDAO.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(authorDTO, AuthorDTO.class);
    }

    public void updateAuthor(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        AuthorDTO authorDTO = authorDAO.updateAuthor(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(authorDTO, Author.class);
    }

    public void deleteByID(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        authorDAO.deleteById(id);
        // response
        ctx.res().setStatus(204);
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
