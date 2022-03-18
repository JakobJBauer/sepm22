package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OwnerJdbcDao implements OwnerDao {

    private JdbcTemplate jdbcTemplate;

    private static final String TABLE = "owner";
    private static final String SQL_GET_ALL = "SELECT * FROM " + TABLE;
    private static final String SQL_CREATE = "INSERT INTO " + TABLE +
            " (firstName, lastName, email) VALUES (?, ?, ?);";

    public OwnerJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Owner> getAllOwners(OwnerSearchParams ownerSearchParams) {
        String request = SQL_GET_ALL;
        ArrayList<Object> sqlParams = new ArrayList<>();

        if (ownerSearchParams.getSearchTerm() != null) {
            request += " WHERE LOWER(firstName) LIKE CONCAT('%', ?, '%')";
            request += " OR LOWER(lastName) LIKE CONCAT('%', ?, '%')";
            request += " OR LOWER(email) LIKE CONCAT('%', ?, '%')";
            sqlParams.add(ownerSearchParams.getSearchTerm().toLowerCase());
            sqlParams.add(ownerSearchParams.getSearchTerm().toLowerCase());
            sqlParams.add(ownerSearchParams.getSearchTerm().toLowerCase());
        }

        if (ownerSearchParams.getResultSize() != null) {
            request += " LIMIT ?";
            sqlParams.add(ownerSearchParams.getResultSize());
        }

        try {
            return jdbcTemplate.query(
                    request,
                    this::mapRow,
                    sqlParams.toArray()
            );
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not query all horses", e);
        }
    }

    @Override
    public Owner createOwner(Owner owner) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, owner.getFirstName());
                stmt.setString(2, owner.getLastName());
                stmt.setString(3, owner.getEmail());
                return stmt;
            }, keyHolder);

            owner.setId(((Number)keyHolder.getKeys().get("id")).longValue());

            return owner;
        } catch (DataAccessException e) {
            throw new PersistenceException("Could not create a new Owner ", e);
        }
    }

    private Owner mapRow(ResultSet result, int rownum) throws SQLException {
        Owner owner = new Owner();
        owner.setId(result.getLong("id"));
        owner.setFirstName(result.getString("firstName"));
        owner.setLastName(result.getString("lastName"));
        owner.setEmail(result.getString("email"));
        return owner;
    }
}
