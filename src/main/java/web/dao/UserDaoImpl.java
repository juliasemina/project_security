package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getUsers() {
        List<User> userList = em.createQuery("select u from User u", User.class).getResultList();
        return userList;
    }

    @Override
    public Set<Role> getAllRols() {
        Set<Role> roleList = new HashSet<>(em.createQuery("select u from Role u", Role.class).getResultList());
        System.out.println("Set " + roleList);
        return roleList;
    }
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User save(User user) {
        System.out.println("Save " + user.getRoles());
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        newUser.setEnabled(user.isEnabled());
        newUser.setPass(bCryptPasswordEncoder.encode(user.getPassword()));

        if (user.getRoles().isEmpty()) {
            newUser.addRole(getRoleByName("user"));
        }
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            newUser.addRole(getRoleByName(role.getName()));
        }

        if (user.getId() == null) {
            em.persist(newUser);
            return newUser;
        } else {
            Long id = user.getId();
            newUser.setId(id);
            em.merge(newUser);
            return newUser;
        }
    }

    @Override
    public User getUserbyId(Long id) {
        TypedQuery<User> userId = em.createQuery("select u from User u where u.id = :id", User.class);
        userId.setParameter("id", id);

        return userId.getResultList().stream().findFirst().orElse(null);
    }

    public Role getRoleByName(String name) {
        TypedQuery<Role> roleName = em.createQuery("select r from Role r where r.name = :name", Role.class);
        roleName.setParameter("name", name);

        return roleName.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public void delete(Long id) {
        em.remove(getUserbyId(id));
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> tp = em.createQuery("select u from User u where u.name = :name", User.class);
        tp.setParameter("name", username);

        return tp.getResultList().stream().findFirst().get();
    }
}
