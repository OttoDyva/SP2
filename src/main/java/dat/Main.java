package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
          ApplicationConfig.startServer(9090);
    }
}