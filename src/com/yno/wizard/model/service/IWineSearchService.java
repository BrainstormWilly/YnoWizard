package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineParcel;

public interface IWineSearchService {

	WinesServiceParcel getWinesByQuery( SearchWinesParcel $parcel );
}
