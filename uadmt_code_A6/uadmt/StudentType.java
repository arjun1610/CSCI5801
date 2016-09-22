package com.umn.csci5801.uadmt;
/**
 * Created by utkarsh on 4/24/2016.
 */
public interface StudentType {
	/**
	 *  this is used to calculate score based on the Student type which is used to get the relevant scores set by the faculty according to their perception 
	 * @param cred - Credential Score object which has all the relevant scores 
	 * @return final value 
	 */
	public float calculateScore(CredentialScore cred);
}

class UnderGrad implements StudentType{
	
	@Override
	public float calculateScore(CredentialScore cred) {
		
		return (float) ((0.25*cred.getGpaScore())+ (0.2*cred.getExtraCurricularScore())+ (0.15*cred.getRecommendationsScore())+ (0.25*cred.getSatScore())+ (0.15*cred.getEssayScore()));
	}
	
}

class Graduate implements StudentType{

	@Override
	public float calculateScore(CredentialScore cred) {
		
		return (float) ((0.20*cred.getWorkExperienceScore())+ (0.3*cred.getGpaScore())+ (0.2*cred.getRecommendationsScore())+ (0.25*cred.getGreScore())+ (0.15*cred.getEssayScore()));
	}
	
}

class Professional implements StudentType{

	@Override
	public float calculateScore(CredentialScore cred) {
		
		return (float) ((0.25*cred.getResearchExperienceScore())+ (0.25*cred.getGpaScore())+ (0.25*cred.getRecommendationsScore())+ (0.15*cred.getEssayScore()));
	}
	
}
