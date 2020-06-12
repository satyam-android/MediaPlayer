package com.player.mediaplayer.engine;

import java.util.List;

import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.objectClass;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class appController {
	private static appController cont = null;
	private Handler mhandler = null;

	private appController() {
		// TODO Auto-generated constructor stub
	}

	public void setcontext(Context ctx) {
	}

	public static appController getInstance() {
		if (cont == null)
			cont = new appController();
		return cont;
	}

	public void init() {
		new LooperThread().start();
	}

	public void addEvent(int whichModule, int whatToDo, Object ob) {

		// whereToGo=where;
		// whichClassInNetwork=whichclass;
		Message m = new Message();
		m.obj = ob;
		m.arg1 = whichModule;
		m.arg2 = whatToDo;
		mhandler.sendMessage(m);
	}

	class LooperThread extends Thread {
		// public Handler mHandler;

		@Override
		public void run() {
			Looper.prepare();

			mhandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// process incoming messages here
					// base.handle().sendMessage(new Message());

					handleEvent(msg.obj, msg.arg1, msg.arg2);
				}
			};

			Looper.loop();
		}
	}

	public void handleEvent(Object ob, int whichModule, int whatToDo) {
		switch (whichModule) {

		case AppConstant.DATABASE:

			switch (whatToDo) {
			case AppConstant.INSERT_TABLE:
				objectClass obj = (objectClass) ob;
				List<String> list = (List<String>) obj.getObj();
				Activity a = obj.getAct();
				SongsTable database = new SongsTable(a);
				database.insertArray(list);

				break;

			default:
				break;
			}

			break;
		default:
			break;
		}

	}

}
