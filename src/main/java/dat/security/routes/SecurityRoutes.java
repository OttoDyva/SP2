package dat.security.routes;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat.utils.Utils;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
public class SecurityRoutes {
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();

    public static EndpointGroup getSecurityRoutes() {
        return () -> {
            path("/auth", () -> {
                get("/users", securityController::getAllUsers, Role.ADMIN); // Get all users
                get("/test", ctx -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from Open Deployment")), Role.ANYONE); // Public test route
                post("/login", securityController.login(), Role.ANYONE); // Login endpoint
                post("/register", securityController.register(), Role.ANYONE); // Register endpoint
                post("/user/addrole", securityController.addRole(), Role.USER); // Add role to user
                delete("/user/{username}", securityController::deleteUser, Role.ADMIN); // Delete user by username
                put("/user/{username}", securityController.editUser(), Role.ADMIN); // Edit user details
            });
        };
    }

    public static EndpointGroup getSecuredRoutes() {
        return () -> {
            path("/protected", () -> {
                get("/user_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from USER Protected")), Role.USER); // User-only route
                get("/admin_demo", (ctx) -> ctx.json(jsonMapper.createObjectNode().put("msg", "Hello from ADMIN Protected")), Role.ADMIN); // Admin-only route
            });
        };
    }
}
