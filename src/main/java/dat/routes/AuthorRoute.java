package dat.routes;

import dat.controllers.AuthorController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthorRoute {

    private final AuthorController authorController = new AuthorController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", authorController::create, Role.USER);
            get("/", authorController::getAllAuthors);
            get("/{id}", authorController::findAuthorById);
            put("/{id}", authorController::updateAuthor);
            delete("/{id}", authorController::deleteByID);
        };
    }
}

