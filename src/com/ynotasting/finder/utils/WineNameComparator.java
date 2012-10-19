package com.ynotasting.finder.utils;

import java.util.Comparator;

import com.ynotasting.finder.model.WineParcel;

public class WineNameComparator implements Comparator<WineParcel> {

	public int compare( WineParcel $wine1, WineParcel $wine2) {
		return $wine2.nameQualified.compareToIgnoreCase( $wine1.nameQualified );
	};
}
