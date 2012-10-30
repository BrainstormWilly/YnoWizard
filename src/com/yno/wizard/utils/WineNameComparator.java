package com.yno.wizard.utils;

import java.util.Comparator;

import com.yno.wizard.model.WineParcel;

public class WineNameComparator implements Comparator<WineParcel> {

	public int compare( WineParcel $wine1, WineParcel $wine2) {
		return $wine2.nameQualified.compareToIgnoreCase( $wine1.nameQualified );
	};
}
