package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.BarsDAO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;


public class Main {
    public static void main(String[] args) {
        //build test
          ApplicationConfig.startServer(9090);
    }
}