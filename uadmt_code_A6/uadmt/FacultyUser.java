package com.umn.csci5801.uadmt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arjun on 4/24/2016.
 */
public class FacultyUser extends User {
    /**
     *  this method fetches applications assigned to the corresponding faculty
     * @return List of Application Objects to review
     * @throws SQLException
     */
    public List<Application> fetchApplications() throws SQLException
    {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        //Prepare query to fetch Applications
        PreparedStatement preparedStatement = null;
        List<Application> applicationsList = new ArrayList<>();
        String query = "SELECT * FROM uadmt.application" +
                "WHERE application_id = (SELECT app_id FROM uadmt.assign_faculty WHERE user_id1 = ? or user_id2 = ? or user_id3= ?";

        try
        {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, _userID);
            preparedStatement.setInt(2, _userID);
            preparedStatement.setInt(3, _userID);
            //
            ResultSet rs_application = preparedStatement.executeQuery();
            //create Application object for the review
            while(rs_application.next())
            {
                // create Application Object here using all the fields;
                int applicationID = rs_application.getInt(Constants.APPLICATION_ID);
                int deptID = rs_application.getInt(Constants.APPLICATION_DEPT_ID);
                int appType = rs_application.getInt(Constants.APPLICATION_TYPE);
                Timestamp date_submitted = rs_application.getTimestamp(Constants.APPLICATION_DATE);
                float appGPA = rs_application.getFloat(Constants.APPLICATION_GPA);
                String recommendations = rs_application.getString(Constants.APPLICATION_RECOMMENDATION);
                int sat = rs_application.getInt(Constants.APPLICATION_SAT);
                int act = rs_application.getInt(Constants.APPLICATION_ACT);
                String essay = rs_application.getString(Constants.APPLICATION_ESSAY);
                float work_ex = rs_application.getFloat(Constants.APPLICATION_WORK_EX);
                int gre = rs_application.getInt(Constants.APPLICATION_GRE);
                float research_ex = rs_application.getFloat(Constants.APPLICATION_RES_EXP);

                applicationsList.add(new Application (applicationID, deptID, appType, date_submitted, appGPA,
                        recommendations, sat, act, essay, work_ex, gre, research_ex));
            }

        }
        catch (SQLException ex)
        {
            System.out.println("Couldn't fetch the applications with exception :" + ex.getMessage()+ " for the user:" +
                    _userID);
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return applicationsList;
    }

    /**
     *  this method initiates a review for an application and returns a review Object attached to an application
     * @param application - An Application object
     * @return - A review Object which has review 
     */
    public Review reviewApplications(Application application, List<Float> scores, Recommendation decision, String comment)
    {
        Review review = new Review();
        try
        {
            review.setReviewScores(scores.get(0),scores.get(1),scores.get(2),scores.get(3),scores.get(4),scores.get(5),scores.get(6),scores.get(7)); //user based input
            review.setRec(decision); //user based input);
            review.calculateScoreBasedOnType(application);
            review.setComment(comment); // user based input
        }
        catch (Exception e)
        {
            System.out.println("Exception occurred in reviewing the Application with ID " + application.get_applicationID()) ;
        }
        return review;
    }
    /**
     *  this is the method to update the review for faculty table for each application ID and user IDs
     * @param applications - List of applicationIDs
     * @return  - DB update successful or not
     * @throws SQLException
     */
    @Override
    public boolean update_review(Application application, Review appReview) throws SQLException
    {

            //AtomicInteger atomicInteger = new AtomicInteger(1000000);
            Connection dbConnection = null;
            DBConnection connect = new DBConnection();
            // Prepare query to Assign Applications
            PreparedStatement preparedStatement = null;
            String query = "INSERT INTO uadmt.Review_Faculty" +
                    "(APPLICATION_ID, SCORE, DECISION_REC, COMMENT)" +
                    "(?,?,?,?)";
            try {
                dbConnection = connect.createDBConnection();
                preparedStatement = dbConnection.prepareStatement(query);
                if (appReview != null) {
                        int appID = application.get_applicationID();
                        float score = appReview.getFinalScore();
                        Recommendation rec = appReview.getRec();
                        String comment = appReview.getComment();
                        preparedStatement.setInt(1, appID);
                        preparedStatement.setFloat(2, score);
                        preparedStatement.setInt(3, rec.getValue());
                        preparedStatement.setString(4, comment);
                        preparedStatement.executeUpdate();
                    }
                else
                    System.out.println("Couldn't get any reviewed Applications results!");
            }
            catch (SQLException ex){
                System.out.println("Couldn't update the application reviews with exception :" + ex.getMessage() + " for the user:" +
                        _userID);
                return false;
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return true;
    }
}
