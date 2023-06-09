package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", (rs, rowNum) -> new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("mpa_name")));
    }

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING WHERE MPA_ID=?", id);
        if (mpaRow.next()) {
            Mpa mpa = new Mpa(
                    mpaRow.getInt("mpa_id"),
                    mpaRow.getString("mpa_name"));
            return mpa;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Рейтинг %d не найден", id));
        }
    }
}
