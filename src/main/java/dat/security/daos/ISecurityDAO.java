package dat.security.daos;

import dat.security.entities.User;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    User addRole(UserDTO user, String newRole);
    List<User> getAllUsers();
    User findUserById(int id);
    void deleteById(Integer integer);
}
