package web.dao;

import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    List<User> getUsers();
    Set<Role> getAllRols();
    User save(User user);
    User getUserbyId(Long id);
    void delete(Long id);
    User findByUsername(String username);
}
