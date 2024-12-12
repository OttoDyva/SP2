package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.BarsDAO;
import dat.security.daos.SecurityDAO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;


public class Main {
    public static void main(String[] args) {
        //build test
        SecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory("bars"));
          ApplicationConfig.startServer(9090);
        System.out.println(securityDAO.getAllUsers());
    }
}