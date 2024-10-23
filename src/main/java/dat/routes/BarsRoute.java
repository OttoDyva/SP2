package dat.routes;

import dat.controllers.BarsController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class BarsRoute {

    private final BarsController barsController = new BarsController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/", barsController::create);
            get("/", barsController::getAllBars);
            get("/{id}", barsController::getBarsById);
            put("/{id}", barsController::updateBars);
            delete("/{id}", barsController::deleteBars);
        };
    }
}
