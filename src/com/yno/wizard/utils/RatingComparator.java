package com.yno.wizard.utils;

import java.util.Comparator;

import com.yno.wizard.model.RatingParcel;

public class RatingComparator implements Comparator<RatingParcel> {

	@Override
	public int compare(RatingParcel lhs, RatingParcel rhs) {
		if( lhs.value<rhs.value) return -1;
		else if( lhs.value>rhs.value ) return 1;
		return 0;
	}

}
