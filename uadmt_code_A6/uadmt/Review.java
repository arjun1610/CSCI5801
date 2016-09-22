package com.umn.csci5801.uadmt;
/**
 * Created by utkarsh on 4/24/2016.
 */
public class Review{
	CredentialScore _cred;
	// default value of the Recommendation
	Recommendation _rec=Recommendation.REJECT;
	// default value of the final score
	float _finalScore=0;
	String _comment;
	//Used by each faculty
    public void setReviewScores(float essayScore_, float extraCurricularScore_, float recommendationScore_,
                                float satScore_, float gpaScore_, float greScore_, float researchExScore_,
                                float workExScore_ )
    {
        _cred.setEssays(essayScore_);
        _cred.setExtraCurricularScore(extraCurricularScore_);
        _cred.setRecommendationsScore(recommendationScore_);
        _cred.setSatScore(satScore_);
        _cred.setGpaScore(gpaScore_);
        _cred.setGreScore(greScore_);
        _cred.setResearchExperienceScore(researchExScore_);
        _cred.setWorkExperienceScore(workExScore_);
    }
	//Used by each faculty
	public void setRec(Recommendation rec_) {
		this._rec = rec_;
	}
	public void setComment(String comment_)
	{
		this._comment = comment_;
	}
	public void calculateScoreBasedOnType(Application ob){
			this._finalScore= ob._type.calculateScore(_cred);
	}
	public void setScore(float score_)
	{
		this._finalScore= score_;
	}
    public CredentialScore getCred() {
        return _cred;
    }

    public Recommendation getRec() {
        return _rec;
    }
    
    public float getFinalScore() {
        return _finalScore;
    }
    public String getComment(){
    	return _comment;
    }
}
