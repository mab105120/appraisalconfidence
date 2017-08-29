package edu.grenoble.em.bourji.db;

import org.skife.jdbi.v2.DBI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Moe on 8/23/17.
 */
public class AppraisalConfidenceDAO {

    private final DBI dbi;

    public AppraisalConfidenceDAO(DBI dbi) {
        this.dbi = dbi;
    }

    public void createTable() throws SQLException {
        Connection connection = dbi.open().getConnection();
        PreparedStatement ps = connection.prepareStatement(getCreateTableQuery());
        ps.execute();
    }

    private String getCreateTableQuery() {
        return "CREATE TABLE message (username varchar(64) NOT NULL, message varchar(200) NOT NULL)";
    }

}
