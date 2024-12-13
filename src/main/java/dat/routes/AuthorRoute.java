package dat.routes;

import dat.controllers.AuthorController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthorRoute {

    private final AuthorController authorController = new AuthorController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", authorController::create, Role.ANYONE);
            get("/", authorController::getAllAuthors, Role.ANYONE);
            get("/{id}", authorController::findAuthorById, Role.ANYONE);
            get("/name/{name}", authorController::findAuthorByName, Role.ANYONE);
            get("/description/{description}", authorController::findAuthorByDescription, Role.ANYONE);
            put("/{id}", authorController::updateAuthor, Role.ADMIN);
            delete("/{id}", authorController::deleteByID, Role.ADMIN);
        };
    }
}