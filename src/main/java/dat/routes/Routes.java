package dat.routes;

import dat.security.enums.Role;
import dat.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    public EndpointGroup getRoutes() {
        return () -> {
            path("/auth", SecurityRoutes.getSecurityRoutes());
        };
    }
}
