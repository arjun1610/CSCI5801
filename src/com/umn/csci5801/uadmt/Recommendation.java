package com.umn.csci5801.uadmt;
/**
 * Created by utkarsh on 4/24/2016.
 */
public enum Recommendation {

	ACCEPT(1),
	WAITLIST (0),
	REJECT (-1);
	private final int value;
	Recommendation(final int newValue) {
		value = newValue;
	}
	public int getValue()
	{
		return value;
	}
}
