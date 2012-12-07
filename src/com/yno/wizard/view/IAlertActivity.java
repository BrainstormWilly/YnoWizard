package com.yno.wizard.view;

import com.yno.wizard.view.assist.ActivityAlertAssist;

public interface IAlertActivity {
	
//	void alertDismissProgress();
//	void alertDismissAlert();
//	void alertShowProgress( int $msg );
//	void alertShowAlert( int $title, int $body, int $id, int $type );
//	void alertAction( int $id, int $action );
	ActivityAlertAssist getAlertAssist();
}
