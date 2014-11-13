package com.asmack.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.asmack.ActivityMain;
import com.asmack.XmppFileManager;
import com.asmack.XmppMessageManager;

public class MessageListenerService extends Service {
	private static XmppMessageManager _xmppMessageMgr;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("info", "Service Bind Success");
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		_xmppMessageMgr = new XmppMessageManager();
		_xmppMessageMgr.initialize(getApplicationContext(), ActivityMain.connection);
		System.out.println("-----messageservice start");
		return START_STICKY;
	}

}
