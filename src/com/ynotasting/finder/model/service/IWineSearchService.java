package com.ynotasting.finder.model.service;

import java.util.ArrayList;

import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.WineParcel;

public interface IWineSearchService {

	ArrayList<WineParcel> getLastUnqualifiedWines();
	
	ArrayList<WineParcel> getLastQualifiedWines();
	
	ArrayList<WineParcel> getWinesByQuery( SearchWinesParcel $parcel );
}
