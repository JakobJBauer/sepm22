package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
public class HorseJdbcDao implements HorseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorseJdbcDao.class);

    private static final String TABLE_HORSE = "horse";
    private static final String TABLE_HORSE_PARENTS = "(SELECT id, name, description, birthdate, sex, ARRAY_AGG(parent)[1] as parent1, (CASE WHEN CARDINALITY(ARRAY_AGG(parent)) >= 2 THEN ARRAY_AGG(parent)[2] ELSE null END) as parent2,  ownerId FROM" +
            " (SELECT id, name, description, birthdate, sex, ownerId, parent FROM " + TABLE_HORSE +
            " LEFT OUTER JOIN child_of ON horse.id = child_of.child)" +
            " group by id)";
    private static final String TABLE_HORSE_PARENTS_OWNER = " (SELECT  horse.id, name, description, birthdate, sex, parent1, parent2, ownerId, firstName, lastName, email FROM " +
            TABLE_HORSE_PARENTS + " as horse " +
            "LEFT JOIN owner ON owner.id = horse.ownerId) ";
    private static final String CHILD_TABLE = "child_of";
    private static final String CHILD_DELETE_BY_CHILD = "DELETE FROM " + CHILD_TABLE + " WHERE child = ?";
    private static final String CHILD_DELETE_BY_ANY = "DELETE FROM " + CHILD_TABLE + " WHERE child = ? OR parent = ?";
    private static final String CHILD_ADD = "INSERT INTO " + CHILD_TABLE + " (child, parent) VALUES (?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_HORSE_PARENTS_OWNER + " WHERE true";
    private static final String SQL_SELECT_PARENTS = "SELECT id, name, sex FROM " + TABLE_HORSE + " WHERE true";
    private static final String SQL_SELECT_ONE = "SELECT * FROM " + TABLE_HORSE_PARENTS_OWNER + " WHERE id = ?";
    private static final String SQL_PARENT_COUNT = "SELECT COUNT(parent) as parentCnt FROM " + CHILD_TABLE + " WHERE child IN (SELECT child FROM child_of where parent = ?) GROUP BY child";
    private static final String SQL_CREATE = "INSERT INTO " + TABLE_HORSE +
            " (name, description, birthdate, sex, ownerId)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE " + TABLE_HORSE +
            " SET name = ?, description = ?, birthdate = ?, sex = ?, ownerId = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE_HORSE + "  WHERE id = ?";
    private static final String SQL_GET_ANCESTOR_TREE = "WITH RECURSIVE ancestor_tree(id, name, birthdate, parent1, parent2, depth) AS (" +
            " SELECT id, name, birthdate, parent1, parent2, 0 FROM " +
            TABLE_HORSE_PARENTS +
            " WHERE id = ?" +
            " UNION ALL" +
            " SELECT hp.id, hp.name, hp.birthdate, hp.parent1, hp.parent2, depth + 1 FROM " +
            TABLE_HORSE_PARENTS + " hp" +
            " INNER JOIN ancestor_tree ht ON" +
            " ht.parent1 = hp.id OR ht.parent2 = hp.id" +
            " WHERE depth < ?" +
            ")" +
            " SELECT * FROM ancestor_tree" +
            " ORDER BY depth DESC";

    private final JdbcTemplate jdbcTemplate;

    public HorseJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Horse> getAll(HorseSearchParams horseSearchParams) {
        LOGGER.trace("getAll({})", horseSearchParams);
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
            sqlRequest += " AND (LOWER(firstName) LIKE CONCAT('%', ?, '%')";
            sqlRequest += " OR LOWER(lastName) LIKE CONCAT('%', ?, '%')";
            sqlRequest += " OR LOWER(email) LIKE CONCAT('%', ?, '%'))";
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
    public List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams) {
        LOGGER.trace("parentOptions({})", parentSearchParams);
        String request = SQL_SELECT_PARENTS;
        ArrayList<Object> sqlParams = new ArrayList<>();

        if (parentSearchParams.getSearchTerm() != null) {
            request += " AND LOWER(name) LIKE CONCAT('%', ?, '%')";
            sqlParams.add(parentSearchParams.getSearchTerm().toLowerCase());
        }

        if (parentSearchParams.getSex() != null) {
            request += " AND sex = ?";
            sqlParams.add(parentSearchParams.getSex().toString());
        }

        if (parentSearchParams.getResultSize() != null) {
            request += " LIMIT ?";
            sqlParams.add(parentSearchParams.getResultSize());
        }

        try {
            return jdbcTemplate.query(
                    request,
                    this::mapRowSearchHorse,
                    sqlParams.toArray()
            );
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }

    @Override
    public List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations) {
        LOGGER.trace("getAncestorTree({})", maxGenerations);
        try {
            return jdbcTemplate.query(
                    SQL_SELECT_ALL,
                    (rs, rowNum) -> this.mapRowAncestorHorse(rs, rowNum, maxGenerations)
            );
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }

    @Override
    public Horse getHorseById(Long id) {
        LOGGER.trace("getHorseById({})", id);
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
        LOGGER.trace("createHorse({})", horse);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                setRequestParams(horse, stmt);
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
    @Transactional
    public Horse updateHorse(Horse horse) {
        LOGGER.trace("updateHorse({})", horse);
        try {
            var entityId = jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_BY_ID);
                setRequestParams(horse, stmt);
                stmt.setObject(6, horse.getId());
                return stmt;
            });

            if (entityId == 0)
                throw new NoResultException(
                        "Could not find a horse with id " + horse.getId()
                );

        } catch (DataAccessException e) {
            throw new PersistenceException("Could not update Horse " + horse.getId(), e);
        }

        removeParentHorses(horse.getId());
        for (var parent : horse.getParentIds())
            addParentHorse(horse.getId(), parent);

        return horse;
    }

    @Override
    public boolean hasCriticalSex(Horse horse) {
        LOGGER.trace("hasCriticalSex({})", horse);
        try {
            var parentCriticals = jdbcTemplate.query(
                    SQL_PARENT_COUNT,
                    (rs, rowNum) -> rs.getInt("parentCnt") > 1,
                    horse.getId()
            );
            for (boolean criticalSex: parentCriticals) {
                if (criticalSex) return true;
            }
            return false;
        } catch (DataAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private void setRequestParams(Horse horse, PreparedStatement stmt) throws SQLException {
        LOGGER.trace("setRequestParams({})", horse);
        stmt.setString(1, horse.getName());
        stmt.setString(2, horse.getDescription());
        stmt.setDate(3, Date.valueOf(horse.getBirthdate()));
        stmt.setString(4, horse.getSex().toString());
        stmt.setObject(5, horse.getOwner() != null ? horse.getOwner().getId() : null);
    }

    @Override
    @Transactional
    public void deleteHorseById(Long id) {
        LOGGER.trace("deleteHorseById({})", id);
        this.removeHorseRelations(id);
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

    private void removeHorseRelations(Long horseId) {
        LOGGER.trace("removeHorsRelations({})", horseId);
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

    private void removeParentHorses(Long horseId) {
        LOGGER.trace("removeParentHorses({})", horseId);
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(CHILD_DELETE_BY_CHILD);
                pstmt.setLong(1, horseId);
                return pstmt;
            });
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not remove parent relationship to horse " + horseId, e);
        }
    }

    private void addParentHorse(Long horseId, Long parentHorseId) {
        LOGGER.trace("addParentHorse({}, {})", horseId, parentHorseId);
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

    private SearchHorse mapRowSearchHorse(ResultSet result, int rownum) throws SQLException {
        LOGGER.trace("mapRowSearchHorse()");
        return new SearchHorse(
                result.getLong("id"),
                result.getString("name"),
                Sex.valueOf(result.getString("sex"))
        );
    }

    private AncestorTreeHorse mapRowAncestorHorse(ResultSet result, int rownum, Integer maxGeneration) throws SQLException {
        LOGGER.trace("mapRowAncestorHorse()");
        var id = result.getLong("id");

        var wrapper = new Object() {
            Integer lastDepth = null;
            List<AncestorTreeHorse> lastDepthHorses = new LinkedList<>();
            List<AncestorTreeHorse> currentDepthHorses = new LinkedList<>();
        };

        jdbcTemplate.query(SQL_GET_ANCESTOR_TREE, (rs, rowNum) -> {
            var currId = rs.getLong("id");

            var currDepth = rs.getInt("depth");
            if (wrapper.lastDepth == null) wrapper.lastDepth = currDepth;
            if (wrapper.lastDepth != currDepth) {
                wrapper.lastDepthHorses = new LinkedList<>(wrapper.currentDepthHorses);
                wrapper.currentDepthHorses = new LinkedList<>();
                wrapper.lastDepth = currDepth;
            }

            var currParents = wrapper.lastDepthHorses.stream().filter((lastHorse) ->
                    {
                        try {
                            return lastHorse.getId().equals(rs.getObject("parent1")) || lastHorse.getId().equals(rs.getObject("parent2"));
                        } catch (SQLException e) {
                            throw new PersistenceException("parents could bot be read ", e);
                        }
                    }
            );

            var currentHorse = new AncestorTreeHorse(
                    currId,
                    rs.getString("name"),
                    rs.getDate("birthdate").toLocalDate(),
                    currParents.toArray(AncestorTreeHorse[]::new)
            );

            wrapper.currentDepthHorses.add(currentHorse);

            return null;
        }, id, maxGeneration);

        return new AncestorTreeHorse(
                id,
                result.getString("name"),
                result.getDate("birthdate").toLocalDate(),
                wrapper.lastDepthHorses.toArray(AncestorTreeHorse[]::new)
        );
    }

    private Horse mapRow(ResultSet result, int rownum) throws SQLException {
        LOGGER.trace("mapRow()");
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

        var parentIds = new LinkedList<Long>();
        if (result.getObject("parent1") != null)
            parentIds.add(result.getLong("parent1"));
        if (result.getObject("parent2") != null)
            parentIds.add(result.getLong("parent2"));
        horse.setParentIds(
                parentIds.toArray(Long[]::new)
        );

        return horse;
    }
}
