package dat;

import dat.config.HibernateConfig;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("bars");
        Javalin app = Javalin.create();
        app.start(9090);
    }
}