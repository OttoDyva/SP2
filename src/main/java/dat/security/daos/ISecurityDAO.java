package dat.security.daos;

import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;

import java.util.List;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    User addRole(UserDTO user, String newRole);
    List<User> getAllUsers();

    User findUserByUsername(String username);

    boolean deleteByUsername(String username);

    Role findOrCreateRole(String roleName);


    void updateUser(String username, String password, List<String> roles);
}
