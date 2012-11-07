package com.yno.wizard.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.widget.EditText;

public class ManualWineValidator {
	
	private List<String> errs = new ArrayList<String>();
	
	public boolean isValidated(){
		return errs.size()==0;
	}

	public void addField( EditText $field, String $errMsg ){
		if( $field.getText().toString().equals("") )
			errs.add($errMsg);
	}
	
	public String toErrString(){
		String str = "";
		Iterator<String> itr = errs.iterator();
		while( itr.hasNext() )
			str += itr.next() + "\n";
		return str;
	}
}
