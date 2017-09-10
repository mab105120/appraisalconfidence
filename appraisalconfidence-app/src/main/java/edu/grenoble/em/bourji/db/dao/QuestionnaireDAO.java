package edu.grenoble.em.bourji.db.dao;

/**
 * Created by Moe on 9/9/2017.
 */
public class QuestionnaireDAO {

    private UserDemographicDAO userDemographicDAO;

    public QuestionnaireDAO withUserDemographicDao(UserDemographicDAO dao) {
        this.userDemographicDAO = dao;
        return this;
    }

    public UserDemographicDAO getUserDemographicDAO() {
        return userDemographicDAO;
    }

}
