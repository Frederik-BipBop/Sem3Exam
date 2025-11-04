package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.populators.Populator;
import dat.routes.Routes;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        var emf = HibernateConfig.getEntityManagerFactory();

        boolean deployed = Boolean.parseBoolean(System.getenv().getOrDefault("DEPLOYED", "false"));
        if (!deployed) {
            Populator.run(emf);
        }

        // 3) Start server
        Javalin app = ApplicationConfig.startServer(7070);

    }
}
