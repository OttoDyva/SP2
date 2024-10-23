package dat;

import dat.config.HibernateConfig;
import dat.daos.AuthorDAO;
import dat.dtos.AuthorDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("bars");
        Javalin app = Javalin.create();
        app.start(9090);

        AuthorDAO authorDAO = new AuthorDAO(emf);

    }
}