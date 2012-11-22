package com.yno.wizard.utils;

import java.util.Comparator;

import com.yno.wizard.model.SearchTypeParcel;

public class WineTypesComparator implements Comparator<SearchTypeParcel> {

	@Override
	public int compare(SearchTypeParcel lhs, SearchTypeParcel rhs) {
		return lhs.name.compareTo(rhs.name);
	}

}
