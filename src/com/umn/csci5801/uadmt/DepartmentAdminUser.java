package com.umn.csci5801.uadmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by arjun on 4/24/2016.
 */
public class DepartmentAdminUser extends User {

    /**
     * this method fetches the applications which has to be assigned to the Faculty in the department
     *
     * @return - List of application IDs
     * @throws SQLException
     */
    public List<Integer> fetchApplications() throws SQLException {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        // Prepare query to fetch Applications
        PreparedStatement preparedStatement = null;
        List<Integer> applicationIDs = new ArrayList<>();

        // TODO-  method filter applications can be used here
        String query = "SELECT application_id FROM uadmt.application" +
                "WHERE dept_id = ? ";
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            // this restricts the user to fetch other department applications 
            preparedStatement.setInt(1, _deptID);
            ResultSet rs_application = preparedStatement.executeQuery();
            // fetch relevant Application IDs for updating the tables for assignment
            if (rs_application == null)
                throw new SQLException();
            else {
                while (rs_application.next()) {
                    int appID = rs_application.getInt(Constants.APPLICATION_ID);
                    applicationIDs.add(appID);
                }
            }
        }
        catch (SQLException ex) {
            System.out.println("Couldn't fetch the applications with exception :" + ex.getMessage() + " for the user:" +
                    _userID);
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return applicationIDs;
    }
    
    /**
     * this method retrieves the faculty IDs for the faculty and department user
     * @param deptID_ - the department ID
     * @param userType_ - the userType
     * @return List of UserIDs which are Faculty Users in the department
     * @throws SQLException
     */
    public List<Integer> getFacultyUserIDs(int deptID_, int userType_) throws SQLException {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        // Prepare query to fetch Faculty IDs
        PreparedStatement preparedStatement = null;
        List<Integer> facultyList = new ArrayList<>();
        String query = "SELECT USER_ID FROM uadmt.User" +
                "WHERE DEPT_ID = ? AND TYPE = ? ";
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, deptID_);
            preparedStatement.setInt(2, userType_);
            ResultSet rs_facultyIDs = preparedStatement.executeQuery();
            if (rs_facultyIDs == null)
                throw new SQLException();
            else {
                while (rs_facultyIDs.next()) {
                    int userID = rs_facultyIDs.getInt(Constants.USER_ID);
                    facultyList.add(userID);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception Occurred.!");
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return facultyList;
    }
    
    /**
     * This method assigns applications to the corresponding faculty Users in the department
     *
     * @param applicationIDs - List of Application IDs
     * @param facultyIDs     - List of faculty UserIDs present in the department
     * @return - DB Update was successful or not
     * @throws SQLException
     */
    public boolean assignApplications(List<Integer> applicationIDs, List<Integer> facultyIDs) throws SQLException {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        // Prepare query to Assign Applications
        PreparedStatement preparedStatement = null;
        // Performs a Insert Operation
        String query = "INSERT INTO uadmt.Assign_Faculty " + "(APPLICATION_ID, USER_ID1, USER_ID2, USER_ID3" +
                "VALUES(?,?,?,?)";
        Random random = new Random();
        int random1, random2, random3;
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            // assign applications to three random faculty IDs 
            for (int appID : applicationIDs) {
                random1 = random.nextInt(facultyIDs.size());
                random2 = random.nextInt(facultyIDs.size());
                random3 = random.nextInt(facultyIDs.size());

                preparedStatement.setInt(1, appID);
                preparedStatement.setInt(2, facultyIDs.get(random1));
                preparedStatement.setInt(3, facultyIDs.get(random2));
                preparedStatement.setInt(4, facultyIDs.get(random3));
                // do DBupdate over the corresponding table ASSIGN_FACULTY
                preparedStatement.executeUpdate();
            }
            return true;
        }
        catch (SQLException e) {
            System.out.println("Couldn't Update Table with exception :" + e.getMessage() + " for the user:" +
                    _userID);
            return false;
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
    }

    /**
     * this method takes the applications IDs which are reviewed by the faculty User and does the average of the score
     *
     * @param applicationIDs - list of applicationIDs which are reviewed
     * @return LinkedHashMap of Applications IDs, mapped with AvgScore
     */
    public Map<Integer, Float> doAverageScoring(List<Integer> applicationIDs) throws SQLException {
        Map<Integer, Float> avgScoreMap = new LinkedHashMap<>();
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        PreparedStatement preparedStatement = null;
        float totalScore, avgScore;
        // this can be optimized to shoot the query all at once
        String query = "SELECT score FROM uadmt.review_faculty where application_id = ? ";
        try {
            // for each application - do average score
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            for (int appID : applicationIDs) {
                totalScore = 0; avgScore=0;
                preparedStatement.setInt(1, appID);
                ResultSet rs_applicationScores = preparedStatement.executeQuery();
                if (rs_applicationScores != null) {
                    while (rs_applicationScores.next()) {
                        totalScore += rs_applicationScores.getFloat(Constants.APPLICATION_SCORE);
                    }
                    List<Integer> facultyID = fetchAssignedFaculty(appID);
                    if(rs_applicationScores.getFetchSize()==1) 
                    	avgScore=totalScore;
                    else 
                    {
                    	if (isReviewed(appID, facultyID.get(0)) && isReviewed(appID, facultyID.get(1)) && isReviewed(appID, facultyID.get(2)))	
                            avgScore = totalScore/3;
                        else
                        	avgScore = totalScore/2 ;
                    }
                    avgScoreMap.put(appID, avgScore);
                }
                else
                    throw new SQLException();
            }
        }
        catch (SQLException ex) {
            System.out.println("Couldn't fetch the application scores with exception :" + ex.getMessage() + " for the user:" +
                    _userID);
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return avgScoreMap;
    }

    /**
     * this method performs majority voting on the applications reviews
     *
     * @param applicationIDs - list of applications ID which have been reviewed by the faculty
     * @return - List of Map which has key value pair of appID, majorityVote
     */
    public Map<Integer, Integer> doMajorityVoting(List<Integer> applicationIDs) throws SQLException {
        Map<Integer, Integer> majorityVotingMap = new LinkedHashMap<>();
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        PreparedStatement preparedStatement = null;
        int totalVotes, majorityVote=0;
        // this can be optimized to shoot the query all at once
        String query = "SELECT decision FROM uadmt.review_faculty where application_id = ? ";
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            // for each application - do majority voting
            for (int appID : applicationIDs) {
                totalVotes = 0;
                preparedStatement.setInt(1, appID);
                ResultSet rs_applicationVotes = preparedStatement.executeQuery();
                if (rs_applicationVotes == null)
                    throw new SQLException();
                else {
                    while (rs_applicationVotes.next()) {
                        totalVotes += rs_applicationVotes.getFloat(Constants.APPLICATION_SCORE);
                    }
                }
                
                if(rs_applicationVotes.getFetchSize()>0 && rs_applicationVotes.getFetchSize()<=3 )
                {
                	if (totalVotes >= 1)
                		majorityVote = 1;
                	else if (totalVotes == 0)
                		majorityVote = 0;
                	else
                		majorityVote = -1;
                }
                majorityVotingMap.put(appID, majorityVote);
            }
        }
        catch (SQLException ex) {
            System.out.println("Couldn't fetch the application scores with exception :" + ex.getMessage() + " for the user:" +
                    _userID);
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return majorityVotingMap;
    }
    /**
     * this method is used to attach the review by Department level User to teh applications 
     * 
     * @param applications - List of applications to be reviewed by the DLA user
     * @param comment - User based input 
     * @return - DB update successful or not
     * @throws SQLException
     */
    public boolean reviewApplications(List<Application> applications, String comment) throws SQLException
    {
    	Map<Integer, Float> mapScore;
        Map<Integer, Integer> mapVote;	
        List<Integer> appIDs = new ArrayList<Integer>();
        float score=0;
        int decision=0;
        for ( Application application: applications)
        	appIDs.add(application.get_applicationID());
        
        mapVote = doMajorityVoting(appIDs);
        mapScore = doAverageScoring(appIDs);
        if(mapVote!=null && mapScore!=null)
        {
        	for(Application application: applications)
        	{
        		Review reviewAppDLA= new Review();
        		decision = mapVote.get(application.get_applicationID());
        		score= mapScore.get(application.get_applicationID());
        		reviewAppDLA.setScore(score);
        		reviewAppDLA.setComment(comment);
        		
        		if( decision > 0)
        			reviewAppDLA.setRec(Recommendation.ACCEPT);
        		else if(decision==0)
        			reviewAppDLA.setRec(Recommendation.WAITLIST);
        		else
        			reviewAppDLA.setRec(Recommendation.REJECT);
        		if(!update_review(application, reviewAppDLA))
        			return false;
        	}
        	return true;        
        }
        else
        	return false;        
    }
    /**
     * this method inserts a row for each application ID along with the review
     * @param applications - List of Applications
     * @return DB update successful or not
     * @throws SQLException
     */
    @Override
    public boolean update_review(Application application, Review appReview) throws SQLException {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        // Prepare query to Assign Applications
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO uadmt.Review_Department" +
                "(APPLICATION_ID, AVG_SCORE, MAJ_VOTE, COMMENT)" +
                "(?,?,?,?,?)";
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
        	Recommendation rec = appReview.getRec();
            preparedStatement.setInt(1, application.get_applicationID());
            preparedStatement.setFloat(2, appReview.getFinalScore());
            preparedStatement.setInt(3, rec.getValue());
            preparedStatement.setString(4, appReview.getComment());
            preparedStatement.executeUpdate();
            return true;
        }
        catch (Exception ex) {
        	 System.out.println("Couldn't update the application for" + application.get_applicationID() + "review with exception :" + ex.getMessage() + " for the user:" + _userID);            
        	 return false;
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
    }
    /**
     * this method fetches the assigned Faculty User IDs for the application ID
     * 
     * @param applicationID : application ID
     * @return - List of Faculty UserIDs assigned for Reviews
     * @throws SQLException
     */
    public List<Integer> fetchAssignedFaculty(int applicationID) throws SQLException {
        Connection dbConnection = null;
        DBConnection connect = new DBConnection();
        // Prepare query to fetch Faculty IDs
        PreparedStatement preparedStatement = null;
        // fetches the relevant assigned three faculty userIDs from the DB for that respective Application IDs
        String query = "SELECT userID1, userID2, userID3 FROM uadmt.Assign_Faculty" +
                "WHERE application_id = ? ";
        List<Integer> facultyList = new ArrayList<>();
        try {
            dbConnection = connect.createDBConnection();
            preparedStatement = dbConnection.prepareStatement(query);
            // this restricts the user to fetch other department applications 
            preparedStatement.setInt(1, applicationID);
            ResultSet rs_facultyIDs = preparedStatement.executeQuery();
            // fetch relevant Application IDs for updating the tables for assignment
            if (rs_facultyIDs == null)
                throw new SQLException();
            else {
                    int userID1 = rs_facultyIDs.getInt(Constants.USER_ID1);
                    int userID2 = rs_facultyIDs.getInt(Constants.USER_ID2);
                    int userID3 = rs_facultyIDs.getInt(Constants.USER_ID3);
                    facultyList.add(userID1);
                    facultyList.add(userID2);
                    facultyList.add(userID3);
                }
            }
        
        catch (SQLException ex) {
            System.out.println("Couldn't fetch the UserIDs with exception :" + ex.getMessage() + " for the user:" +
                    _userID);
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (dbConnection != null)
                dbConnection.close();
        }
        return facultyList;
    }
    /**
     * this method checks if the user has reviewed the application 
     * @param appID - application ID
     * @param userID - userID assigned for review
     * @return - has reviewed or not
     * @throws SQLException
     */
    public boolean isReviewed (int appID, int userID) throws SQLException
    {
    	  Connection dbConnection = null;
          DBConnection connect = new DBConnection();
          // Prepare query to fetch Application Reviews
          PreparedStatement preparedStatement = null;
          // fetches the review row if the user has already reviewed the application ID
          String query = "SELECT * FROM uadmt.Review_Faculty" +
                  "WHERE application_id = ?  and user_id = ?";
          try {
              dbConnection = connect.createDBConnection();
              preparedStatement = dbConnection.prepareStatement(query);
              // this restricts the user to fetch other department applications 
              preparedStatement.setInt(1, appID);
              preparedStatement.setInt(1, userID);
              ResultSet rs_rows = preparedStatement.executeQuery();
              // fetch relevant Application IDs for updating the tables for assignment
              if (rs_rows.getFetchSize()>0 && rs_rows != null)
                  return true;
              else 
            	  return false;
         }
         
          catch (SQLException ex) {
              System.out.println("Couldn't fetch the reviewed:" + ex.getMessage() + " for the user:" +
                      _userID);
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
