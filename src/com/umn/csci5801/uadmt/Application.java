package com.umn.csci5801.uadmt;

import java.sql.Timestamp;

/**
 * Created by arjun on 4/21/2016.
 */
public class Application {

    public int _applicationID;
    public StudentType  _type ;
    public int _deptID;
    public Timestamp _date_submitted;
    public float _gpa;
    public String _recommendations;
    public int _sat;
    public int _act;
    public String _essay;
    public float _work_ex;
    public int _gre;
    public float _research_ex;

    //Setting type for the student by using info from the applicant DB
    /**
     * 
     * @param newType_ = Integer Type set fetched from the Database
     */
    public void setType (int newType_){
        switch(newType_)
        {
            case 1: this._type = new UnderGrad(); break;
            case 2: this._type = new Graduate(); break;
            case 3: this._type = new Professional(); break;
        }
    }
    
    public Application(int applicationID_, int type_, int deptID_, Timestamp date_submitted_, float gpa_, String recommendations_, int sat_, int act_, String essay_, float work_ex_, int gre_, float research_ex_) {
        this._applicationID = applicationID_;
        this.setType(type_);
        this._deptID = deptID_;
        this._date_submitted = date_submitted_;
        this._gpa = gpa_;
        this._recommendations = recommendations_;
        this._sat = sat_;
        this._act = act_;
        this._essay = essay_;
        this._work_ex = work_ex_;
        this._gre = gre_;
        this._research_ex = research_ex_;
    }

    public int get_applicationID() {
        return _applicationID;
    }

    public StudentType get_type() {
        return _type;
    }

    public int get_deptID() {
        return _deptID;
    }

    public Timestamp get_date_submitted() {
        return _date_submitted;
    }

    public float get_gpa() {
        return _gpa;
    }

    public String get_recommendations() {
        return _recommendations;
    }

    public int get_sat() {
        return _sat;
    }

    public int get_act() {
        return _act;
    }

    public String get_essay() {
        return _essay;
    }

    public float get_work_ex() {
        return _work_ex;
    }

    public int get_gre() {
        return _gre;
    }

    public float get_research_ex() {
        return _research_ex;
    }
}
