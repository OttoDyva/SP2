package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

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
        AuthorDTO authorDTO = authorDAO.findAuthorById(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(authorDTO, AuthorDTO.class);
    }

    public boolean validatePrimaryKey(Integer integer) {
        return authorDAO.validatePrimaryKey(integer);
    }
}
