package com.ynotasting.finder.utils;

import java.util.Comparator;

import com.ynotasting.finder.model.PriceParcel;

public class PriceComparator implements Comparator<PriceParcel> {

	@Override
	public int compare(PriceParcel lhs, PriceParcel rhs) {
		// TODO Auto-generated method stub
		if( lhs.value<rhs.value) return -1;
		else if( lhs.value>rhs.value ) return 1;
		return 0;
	}

}
