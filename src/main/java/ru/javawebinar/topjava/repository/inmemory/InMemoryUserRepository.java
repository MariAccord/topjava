package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.ROLE_USER);
        save(new User(null, "User", "user@gmail.com", "user111",
                2000, true, userRoles));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ROLE_ADMIN);
        save(new User(null, "Admin", "admin@gmail.com", "admin111",
                2000, true, adminRoles));
    }

    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id)  {
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        return repository.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return getAll().stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst().orElse(null);

    }
}
