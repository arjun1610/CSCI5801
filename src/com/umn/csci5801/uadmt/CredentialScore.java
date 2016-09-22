package com.umn.csci5801.uadmt;
/**
 * Created by utkarsh on 4/24/2016.
 */
public class CredentialScore {

	//Scores given by faculty out of 100
	private float gpaScore;
	private float greScore;
	private float workExperienceScore;
	private float researchExperienceScore;
	private float extraCurricularScore;
	private float recommendationsScore;
	private float essayScore;
	private float satScore;
	// private float actScore;

	public float getGpaScore() {
		return gpaScore;
	}
	public void setGpaScore(float gpaScore) {
		this.gpaScore = gpaScore;
	}
	public float getGreScore() {
		return greScore;
	}
	public void setGreScore(float greScore) {
		this.greScore = greScore;
	}
	public float getWorkExperienceScore() {
		return workExperienceScore;
	}
	public void setWorkExperienceScore(float workExperienceScore) {
		this.workExperienceScore = workExperienceScore;
	}
	public float getResearchExperienceScore() {
		return researchExperienceScore;
	}
	public void setResearchExperienceScore(float researchExperienceScore) {
		this.researchExperienceScore = researchExperienceScore;
	}
	public float getExtraCurricularScore() {
		return extraCurricularScore;
	}
	public void setExtraCurricularScore(float extraCurricularScore) {
		this.extraCurricularScore = extraCurricularScore;
	}
	public float getRecommendationsScore() {
		return recommendationsScore;
	}
	public void setRecommendationsScore(float recommendationsScore) {
		this.recommendationsScore = recommendationsScore;
	}
	public float getEssayScore() {return essayScore;}
	public void setEssays(float essays) {
		this.essayScore = essays;
	}
	public float getSatScore() {
		return satScore;
	}
	public void setSatScore(float satScore) {
		this.satScore = satScore;
	}
}
