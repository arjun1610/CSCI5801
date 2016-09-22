package com.umn.csci5801.uadmt;

import java.sql.SQLException;

/**
 * Created by arjun on 4/24/2016.
 */
public abstract class User {
    public int _userID ;
    public int _deptID ;
    public int _userType ;
    
    public int get_userID() {
        return _userID;
    }

    public int get_deptID() {
        return _deptID;
    }

    public int get_userType() {
        return _userType;
    }
    

    /**
     *
     * @param params - List of the parameter fields
     * @return - Application Object along 
     * @throws SQLException
     */
    // based on queries //List of fields as parameters
    public Application filterApplication(String params) throws SQLException{
        // filters the applications based on the given parameters fields
    	// required if the user performs a filter based on the scores 
        return null;
    }

    /**
     *  This is the abstract level update_review method which is overridden by child classes DepartmentLevelAdmin User and FacultyUser 
     * @param application - Application Object
     * @param review - Review Object
     * @return boolean if the DB update happened or not
     * @throws SQLException
     */
    public abstract boolean update_review(Application application, Review review) throws SQLException;
}
