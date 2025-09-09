package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

@Primary
@Repository
@RequiredArgsConstructor
public class FriendsRepository implements FriendsStorage {
    private final JdbcTemplate jdbc;

    private static final String ADD_FRIEND_QUERY = """
            INSERT INTO friends (
                offering_person_id,
                recipient_person_id
            )
            VALUES (?, ?)
            """;

    private static final String REMOVE_FRIEND_QUERY = """
            DELETE FROM friends
            WHERE offering_person_id = ?
              AND recipient_person_id = ?
            """;

    public void addFriend(Long offeringPersonId, Long recipientPersonId) {
        jdbc.update(ADD_FRIEND_QUERY, offeringPersonId, recipientPersonId);
    }

    public void removeFriend(Long offeringPersonId, Long recipientPersonId) {
        jdbc.update(REMOVE_FRIEND_QUERY, offeringPersonId, recipientPersonId);
    }
}