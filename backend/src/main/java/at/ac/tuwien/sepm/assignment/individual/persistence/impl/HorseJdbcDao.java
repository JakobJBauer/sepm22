package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final String TABLE_NAME = "horse";
    private static final String JOINED_TABLE = "(SELECT h.id, h.name, h.description, h.birthdate, h.sex, h.ownerId, o.firstName, o.lastName, o.email FROM horse as h LEFT OUTER JOIN owner as o on h.ownerId = o.id)";
    private final static String JOINED_TABLE = "(SELECT horse.id, name, description, birthdate, sex, parents, ownerId, firstname, lastname, email  FROM (\n" +
            "        SELECT rs1.id, name, description, birthdate, sex, GROUP_CONCAT(parent SEPARATOR ',') as parents,  ownerId FROM\n" +
            "        (SELECT id, name, description, birthdate, sex, ownerId, parent FROM HORSE \n" +
            "        LEFT OUTER JOIN child_of ON horse.id = child_of.child) as rs1\n" +
            "group by id\n" +
            ")  as horse\n" +
            "LEFT JOIN owner on horse.ownerId = owner.id)";

    private static final String CHILD_TABLE = "child_of";
    private static final String CHILD_DELETE_BY_CHILD = "DELETE FROM " + CHILD_TABLE + " WHERE child = ?";
    private static final String CHILD_DELETE_BY_ANY = "DELETE FROM " + CHILD_TABLE + " WHERE child = ? OR parent = ?";
    private static final String CHILD_ADD = "INSERT INTO " + CHILD_TABLE + " (child, parent) VALUES (?, ?)";
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
    @Transactional
    public Horse createHorse(Horse horse) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, horse.getName());
                stmt.setString(2, horse.getDescription());
                stmt.setDate(3, Date.valueOf(horse.getBirthdate()));
                stmt.setString(4, horse.getSex().toString());
                stmt.setObject(5, horse.getOwner() != null ? horse.getOwner().getId() : null);
                return stmt;
            }, keyHolder);

            horse.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not create a new Horse ", e);
        }

        for (var parent : horse.getParentIds())
            addParentHorse(horse.getId(), parent);

        return horse;
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

        removeParentHorses(horse.getId());
        for (var parent : horse.getParentIds())
            addParentHorse(horse.getId(), parent);

        return horse;
    }

    @Override
    public void deleteHorseById(Long id) {
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

        removeHorseRelations(id);
    }

    private void removeHorseRelations(Long horseId) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(CHILD_DELETE_BY_ANY);
                pstmt.setLong(1, horseId);
                pstmt.setLong(2, horseId);
                return pstmt;
            });
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not remove all relationships to horse " + horseId, e);
        }
    }
    private void addParentHorse(Long horseId, Long parentHorseId) {
        if (parentHorseId == null) return;
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(CHILD_ADD);
                pstmt.setLong(1, horseId);
                pstmt.setLong(2, parentHorseId);
                return pstmt;
            });
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not add parent relationship to horse " + horseId, e);
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
        if (result.getString("parents") != null) {
            var parents = Arrays.stream(result.getString("parents").split(",")).map(Long::valueOf);
            horse.setParentIds(parents.toArray(Long[]::new));
        }
        return horse;
    }
}
