// dat.routes.CandidateRoutes.java
package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.CandidateController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public final class CandidateRoutes {
    private CandidateRoutes(){}

    public static EndpointGroup getRoutes() {
        var emf = HibernateConfig.getEntityManagerFactory();
        var ctrl = new CandidateController(emf); // din controller forventer kun emf

        return () -> {
            // GET /candidates
            get("/", ctrl::readAll, Role.ANYONE);

            // GET /candidates/{id}
            get("/{id}", ctrl::read, Role.ANYONE);

            // POST /candidates
            post("/", ctrl::create, Role.ADMIN);

            // PUT /candidates/{id}
            put("/{id}", ctrl::update, Role.ADMIN);

            // DELETE /candidates/{id}
            delete("/{id}", ctrl::delete, Role.ADMIN);

            // PUT /candidates/{candidateId}/skills/{skillId}
            put("/{candidateId}/skills/{skillId}", ctrl::addSkill, Role.ADMIN);

            // DELETE /candidates/{candidateId}/skills/{skillId}
            delete("/{candidateId}/skills/{skillId}", ctrl::removeSkill, Role.ADMIN);
        };
    }
}
