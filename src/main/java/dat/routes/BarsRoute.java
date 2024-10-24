package dat.routes;

import dat.controllers.BarsController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class BarsRoute {

    private final BarsController barsController = new BarsController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/", barsController::create, Role.USER, Role.ADMIN);
            get("/", barsController::getAllBars, Role.ANYONE);
            get("/{id}", barsController::getBarsById, Role.ANYONE);
            get("/title/{title}", barsController::getBarsByTitle, Role.ANYONE);
            get("/genre/{genre}", barsController::getBarsByGenre, Role.ANYONE);
            put("/{id}", barsController::updateBars, Role.ADMIN);
            delete("/{id}", barsController::deleteBars, Role.ADMIN);
        };
    }
}
