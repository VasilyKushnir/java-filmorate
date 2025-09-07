package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            "WHERE id = ?";

    private static final String FIND_USER_FRIENDS_QUERY = """
            SELECT u.*
            FROM friends AS f
            JOIN users AS u ON u.id = f.recipient_person_id
            WHERE f.offering_person_id = ?
            """;

    private static final String FIND_COMMON_FRIENDS_QUERY = """
            (SELECT u.*
            FROM friends AS f
            JOIN users AS u ON u.id = f.recipient_person_id
            WHERE f.offering_person_id = ?)
            INTERSECT
            (SELECT u.*
            FROM friends AS f
            JOIN users AS u ON u.id = f.recipient_person_id
            WHERE f.offering_person_id = ?)
            """;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> getUser(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public List<User> fetchFriendsList(Long userId) {
        return findMany(FIND_USER_FRIENDS_QUERY, userId);
    }

    public List<User> fetchCommonFriendsList(Long offeringPersonId, Long recipientPersonId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, offeringPersonId, recipientPersonId);
    }
}