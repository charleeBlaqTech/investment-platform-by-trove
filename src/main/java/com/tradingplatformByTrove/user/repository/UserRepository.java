package com.tradingplatformByTrove.user.repository;

import com.tradingplatformByTrove.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-Memory User storage layer.
 * public interface UserRepository extends JpaRepository<User, UUID> {
 */
@Repository
public class UserRepository {
    private final Map<UUID, User> storage = new ConcurrentHashMap<>();

    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID());
        }
        storage.put(user.getUserId(), user);
        return user;
    }

    public List<User> saveAll(Iterable<User> users) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            result.add(save(user));
        }
        return result;
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}