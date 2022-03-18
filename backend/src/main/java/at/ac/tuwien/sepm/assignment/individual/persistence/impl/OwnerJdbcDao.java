package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
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
@Repository
public class OwnerJdbcDao implements OwnerDao {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE = "INSERT INTO " + TABLE +
            " (firstName, lastName, email) VALUES (?, ?, ?);";

    public OwnerJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

}
