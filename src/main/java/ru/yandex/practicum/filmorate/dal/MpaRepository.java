package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class MpaRepository extends BaseRepository<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM mpa
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM mpa
            WHERE id = ?
            """;

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Mpa> findMpa(Integer mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }
}