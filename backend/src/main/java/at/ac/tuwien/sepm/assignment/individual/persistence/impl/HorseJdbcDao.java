package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.Sex;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final String TABLE_NAME = "horse";
    private static final String JOINED_TABLE = "(SELECT h.id, h.name, h.description, h.birthdate, h.sex, h.ownerId, o.firstName, o.lastName, o.email FROM horse as h LEFT OUTER JOIN owner as o on h.ownerId = o.id)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + JOINED_TABLE + " WHERE true";
    private static final String SQL_SELECT_ONE = "SELECT * FROM " + JOINED_TABLE + " WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO " + TABLE_NAME +
            " (name, description, birthdate, sex, ownerId)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE " + TABLE_NAME +
            " SET name = ?, description = ?, birthdate = ?, sex = ?, ownerId = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + "  WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public HorseJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Horse> getAll(HorseSearchParams horseSearchParams) {
        String sqlRequest = SQL_SELECT_ALL;
        ArrayList<Object> sqlParams = new ArrayList<>();

        if (horseSearchParams.getName() != null) {
            sqlRequest += " AND LOWER(name) LIKE CONCAT('%', ?, '%')";
            sqlParams.add(horseSearchParams.getName().toLowerCase());
        }
        if (horseSearchParams.getDescription() != null) {
            sqlRequest += " AND LOWER(description) LIKE CONCAT('%', ?, '%')";
            sqlParams.add(horseSearchParams.getDescription().toLowerCase());
        }
        if (horseSearchParams.getBirthdate() != null) {
            sqlRequest += " AND birthdate <= ?";
            sqlParams.add(horseSearchParams.getBirthdate());
        }
        if (horseSearchParams.getSex() != null) {
            sqlRequest += " AND sex = ?";
            sqlParams.add(horseSearchParams.getSex().toString());
        }
        if (horseSearchParams.getOwnerName() != null) {
            sqlRequest += " AND LOWER(firstName) LIKE CONCAT('%', ?, '%')";
            sqlRequest += " OR LOWER(lastName) LIKE CONCAT('%', ?, '%')";
            sqlRequest += " OR LOWER(email) LIKE CONCAT('%', ?, '%')";
            sqlParams.add(horseSearchParams.getOwnerName().toLowerCase());
            sqlParams.add(horseSearchParams.getOwnerName().toLowerCase());
            sqlParams.add(horseSearchParams.getOwnerName().toLowerCase());
        }

        try {
            return jdbcTemplate.query(
                    sqlRequest,
                    this::mapRow,
                    sqlParams.toArray()
            );
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }

    @Override
    public Horse getHorseById(long id) {
        try {
            var horses = jdbcTemplate.query(SQL_SELECT_ONE, this::mapRow, id);

            if (horses.isEmpty())
                throw new NoResultException("Could not find horse with id " + id);

            return horses.get(0);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not read Horse " + id, e);
        }
    }

    @Override
    public Horse createHorse(Horse horse) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, horse.getName());
                stmt.setString(2, horse.getDescription());
                stmt.setDate(3, Date.valueOf(horse.getBirthdate()));
                stmt.setString(4, horse.getSex().toString());
                stmt.setLong(5, horse.getOwner().getId());
                return stmt;
            }, keyHolder);

            horse.setId(((Number) keyHolder.getKeys().get("id")).longValue());

            return horse;
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not create a new Horse ", e);
        }
    }

    @Override
    public Horse updateHorse(Horse horse) {
        try {
            var entityId = jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_BY_ID);
                stmt.setString(1, horse.getName());
                stmt.setString(2, horse.getDescription());
                stmt.setDate(3, Date.valueOf(horse.getBirthdate()));
                stmt.setString(4, horse.getSex().toString());
                stmt.setLong(5, horse.getOwner().getId());
                stmt.setLong(6, horse.getId());
                return stmt;
            });

            if (entityId == 0)
                throw new NoResultException(
                        "Could not find a horse with id " + horse.getId()
                );

            return horse;
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not update Horse " + horse.getId(), e);
        }
    }

    @Override
    public void deleteHorseById(long id) {
        try {
            var deletedHorseId = jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_BY_ID);
                stmt.setLong(1, id);
                return stmt;
            });
            if (deletedHorseId == 0)
                throw new NoResultException("Could not delete Horse " + id);
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not delete Horse " + id, e);
        }
    }

    private Horse mapRow(ResultSet result, int rownum) throws SQLException {
        Horse horse = new Horse();
        horse.setId(result.getLong("id"));
        horse.setName(result.getString("name"));
        horse.setDescription(result.getString("description"));
        horse.setBirthdate(result.getDate("birthdate").toLocalDate());
        horse.setSex(Sex.valueOf(result.getString("sex")));
        if (result.getLong("ownerId") != 0) {
            horse.setOwner(new Owner(
                    result.getLong("ownerId"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("email")
            ));
        }
        return horse;
    }
}
