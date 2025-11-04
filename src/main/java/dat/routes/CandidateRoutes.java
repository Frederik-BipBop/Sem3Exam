// dat.routes.CandidateRoutes.java
package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.CandidateController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public final class CandidateRoutes {
    private CandidateRoutes() {
    }

    public static EndpointGroup getRoutes() {
        var emf = HibernateConfig.getEntityManagerFactory();
        var ctrl = new CandidateController(emf);
        return () -> {
            // Collection
            get("/", ctrl::readAll, Role.ANYONE);
            // GET /candidates/filter?category={category}
            get("/filter", ctrl::getByCategory, Role.ANYONE);
            // Item
            get("/{id}", ctrl::read, Role.ANYONE);
            // Mutations
            post("/", ctrl::create, Role.ADMIN);
            put("/{id}", ctrl::update, Role.ADMIN);
            delete("/{id}", ctrl::delete, Role.ADMIN);
            // Linking
            put("/{candidateId}/skills/{skillId}", ctrl::addSkill, Role.ADMIN);
            // Unlinking
            delete("/{candidateId}/skills/{skillId}", ctrl::removeSkill, Role.ADMIN);
        };
    }
}
