package dat.config;

import dat.exceptions.DatabaseException;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.enums.Role;
import dat.exceptions.ApiException;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static final Routes routes = new Routes();
    private static final AccessController accessController = new AccessController();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static int count = 1;

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        app.beforeMatched(accessController::accessHandler);
        app.after(ApplicationConfig::afterRequest);

        exceptions(app);

        app.start(port);
        return app;
    }

    private static void afterRequest(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        logger.info(" Request {} - {} was handled with status code {}", count++, requestInfo, ctx.status());
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static Javalin exceptions(Javalin app){
        app.exception(Exception.class, ExceptionHandler::exceptionHandler);
        app.exception(ApiException.class, ExceptionHandler::apiExceptionHandler);
        app.exception(DatabaseException.class, ExceptionHandler::DataBaseExceptionHandler);
        app.exception(UnauthorizedResponse.class, ExceptionHandler::UnauthorizedResponseHandler);
        return app;
    }
}
