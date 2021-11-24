package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> getUsers();

    void save(User user);

    User getUserbyId(Long id);

    void delete(Long id);

    Set<Role> getAllRols();

    User findByUsername(String username);
}
