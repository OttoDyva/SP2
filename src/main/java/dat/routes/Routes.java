package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final AuthorRoute authorRoute = new AuthorRoute();
    private final BarsRoute barsRoute = new BarsRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/bars", barsRoute.getRoutes());
                path("/authors", authorRoute.getRoutes());
        };
    }
}
