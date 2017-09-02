package edu.grenoble.em.bourji.db;

import edu.grenoble.em.bourji.api.JobFunctionReview;
import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import org.skife.jdbi.v2.DBI;

import java.sql.*;

/**
 * Created by Moe on 8/23/17.
 */
public class AppraisalConfidenceDAO {

    private final DBI dbi;

    public AppraisalConfidenceDAO(DBI dbi) {
        this.dbi = dbi;
    }

    public TeacherDossiers getTeacherDossiers(String profile1, String profile2) throws SQLException {
        return new TeacherDossiers(getTeacherDossier(profile1), getTeacherDossier(profile2));
    }

    private TeacherDossier getTeacherDossier(String code) throws SQLException {
        JobFunctionReview studentLearning = new JobFunctionReview();
        JobFunctionReview instructionalPractice = new JobFunctionReview();
        JobFunctionReview professionalism = new JobFunctionReview();
        try(Connection connection = dbi.open().getConnection();
        Statement statement = connection.createStatement()) {
            String query = "SELECT ID, JOB_FUNCTION, SUPERVISOR, REVIEW FROM TEACHER_DOSSIERS WHERE ID = '" + code + "'";
            ResultSet rs  = statement.executeQuery(query);
            while(rs.next()) {
                String jobFunction = rs.getString("JOB_FUNCTION");
                String supervisor = rs.getString("SUPERVISOR");
                String review = rs.getString("REVIEW");
                switch(jobFunction) {
                    case "SL":
                        switch(supervisor.toLowerCase()) {
                            case "sp1":
                                studentLearning.setSupervisor1Review(review);
                                break;
                            case "sp2":
                                studentLearning.setSupervisor2Review(review);
                                break;
                            case "sp3":
                                studentLearning.setSupervisor3Review(review);
                                break;
                        }
                        break;
                    case "IP":
                        switch(supervisor.toLowerCase()) {
                            case "sp1":
                                instructionalPractice.setSupervisor1Review(review);
                                break;
                            case "sp2":
                                instructionalPractice.setSupervisor2Review(review);
                                break;
                            case "sp3":
                                instructionalPractice.setSupervisor3Review(review);
                                break;
                        }
                        break;
                    case "PF":
                        switch(supervisor.toLowerCase()) {
                            case "sp1":
                                professionalism.setSupervisor1Review(review);
                                break;
                            case "sp2":
                                professionalism.setSupervisor2Review(review);
                                break;
                            case "sp3":
                                professionalism.setSupervisor3Review(review);
                                break;
                        }
                        break;
                }
            }
        }

        return new TeacherDossier(studentLearning, instructionalPractice, professionalism);
    }

    public void createTable() throws SQLException {
        Connection connection = dbi.open().getConnection();
        PreparedStatement ps = connection.prepareStatement(getCreateTableQuery());
        ps.execute();
    }

    private String getCreateTableQuery() {
        return "CREATE TABLE TEACHER_DOSSIERS (ID varchar(10) NOT NULL, " +
                "JOB_FUNCTION varchar(200) NOT NULL, SUPERVISOR varchar(64) NOT NULL, REVIEW varchar(500) NOT NULL" +
                ")";
    }
}