package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.daos.BarsDAO;
import dat.dtos.AuthorDTO;
import dat.dtos.BarsDTO;
import dat.entities.Author;
import dat.entities.Bars;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarsController {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("bars");
    private BarsDAO barsDAO = new BarsDAO(emf);
    private AuthorDAO authorDAO = new AuthorDAO(emf);

    public BarsController() {
    }

    public void getAllBars(Context ctx) {
        List<Bars> barsList = barsDAO.getAllBars();
        List<BarsDTO> barsDTOList = barsList.stream()
                .map(BarsDTO::new)
                .collect(Collectors.toList());

        ctx.res().setStatus(200);
        ctx.json(barsDTOList);
    }


    public void getBarsById(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        Bars bars = barsDAO.findBarsById(id);
        if (bars == null) {
            ctx.res().setStatus(404);
            ctx.json("Bars not found");
            return;
        }

        BarsDTO barsDTO = new BarsDTO(bars);
        System.out.println(barsDTO);
        ctx.res().setStatus(200);
        ctx.json(barsDTO, BarsDTO.class);
    }


    public void create(Context ctx) {
        BarsDTO barsDTO = ctx.bodyAsClass(BarsDTO.class);

        Author author = new Author();
        author.setName(barsDTO.getAuthorName());
        author.setDescription(barsDTO.getAuthorDescription());

        AuthorDTO createdAuthorDTO = authorDAO.create(new AuthorDTO(author));

        Bars bars = new Bars();
        bars.setTitle(barsDTO.getTitle());
        bars.setContent(barsDTO.getContent());
        bars.setDate(barsDTO.getDate());
        bars.setGenre(barsDTO.getGenre());
        bars.setAuthor(new Author(createdAuthorDTO));

        barsDAO.create(bars);

        ctx.res().setStatus(201);
        ctx.json(new BarsDTO(bars));
    }



    public void updateBars(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        BarsDTO barsDTO = validateEntity(ctx);



        //barsToUpdate.setId(id);

        //Bars updatedBars = barsDAO.updateBars(barsToUpdate);

        ctx.res().setStatus(200);
        //ctx.json(new BarsDTO(updatedBars));
    }


    public void deleteBars(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        barsDAO.deleteById(id);

        ctx.res().setStatus(200);
        ctx.json(Map.of("message", "Bar with id " + id + " successfully deleted"));
    }


    public boolean validatePrimaryKey(Integer integer) {
        return barsDAO.findBarsById(integer) != null; // Ensure the ID exists
    }

    public BarsDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(BarsDTO.class)
                .check(b -> b.getTitle() != null && !b.getTitle().isEmpty(), "Title must be set")
                .check(b -> b.getContent() != null && !b.getContent().isEmpty(), "Content must be set")
                .check(b -> b.getDate() != null, "Date must be set")
                //.check(b -> b.getGenre() != null && !b.getGenre().isEmpty(), "Genre must be set")
                .get();
    }
}
