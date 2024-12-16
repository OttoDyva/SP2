package dat.security.daos;


import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
public class SecurityDAO implements ISecurityDAO {

    private static ISecurityDAO instance;
    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username);
            user.getRoles().size();
            if (!user.verifyPassword(password))
                throw new ValidationException("Wrong password");
            return new UserDTO(user.getUsername(), user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet()));
        }
    }


    @Override
    public User createUser(String username, String password) {
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, username);
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + username + " already exists");
            userEntity = new User(username, password);
            em.getTransaction().begin();
            Role userRole = em.find(Role.class, "user");
            if (userRole == null)
                userRole = new Role("user");
            em.persist(userRole);
            userEntity.addRole(userRole);
            em.persist(userEntity);
            em.getTransaction().commit();
            return userEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public User addRole(UserDTO userDTO, String newRole) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userDTO.getUsername());
            if (user == null) {
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());
            }

            Role role = findOrCreateRole(newRole);
            if (!user.getRoles().contains(role)) {
                user.addRole(role);
                em.merge(user);
            }

            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApiException(500, "Failed to add role: " + newRole);
        } finally {
            em.close();
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("FROM User", User.class);
            return query.getResultList();
        }
    }

    @Override
    public User findUserByUsername(String username) {
        try (EntityManager em = getEntityManager()) {
            return em.find(User.class, username);
        }
    }

    @Override
    public boolean deleteByUsername(String username) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user != null) {
                em.getTransaction().begin();
                em.remove(user);
                em.getTransaction().commit();
                return true;
            }
            return false;
        }
    }

    @Override
    public Role findOrCreateRole(String roleName) {
        try (EntityManager em = getEntityManager()) {
            Role role = em.find(Role.class, roleName);
            if (role == null) {
                em.getTransaction().begin();
                role = new Role(roleName);
                em.persist(role);
                em.getTransaction().commit();
            }
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(500, "Failed to find or create role: " + roleName);
        }
    }

    @Override
    public void updateUser(String username, String password, List<String> roles) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            User user = em.find(User.class, username);
            if (user == null) {
                throw new EntityNotFoundException("No user found with username: " + username);
            }

            if (password != null && !password.isEmpty()) {
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            }

            if (roles != null && !roles.isEmpty()) {
                user.getRoles().clear();

                for (String roleName : roles) {
                    Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", roleName)
                            .getResultStream()
                            .findFirst()
                            .orElseGet(() -> {
                                Role newRole = new Role(roleName);
                                em.persist(newRole);
                                return newRole;
                            });

                    user.addRole(role);
                }
            }

            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ApiException(500, "Failed to update user: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

