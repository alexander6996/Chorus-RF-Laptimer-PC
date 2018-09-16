package com.chorusrflaptimer.wrapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Handler{

	public abstract void handleMessage(Message msg);

	public final boolean sendEmptyMessage(int what) {
		return sendEmptyMessageDelayed(what, 0);
	}

	public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = new Message();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }
    
	public final boolean sendMessageDelayed(Message msg, long delayMillis) {
		if (delayMillis < 0) {
			delayMillis = 0;
		}
		//return sendMessageAtTime(msg, System.currentTimeMillis() + delayMillis);
		return sendMessageAtTime(msg, delayMillis);
	}
    
	Timer timer;
    public boolean sendMessageAtTime(final Message msg, long uptimeMillis) {
		timer = new Timer((int)uptimeMillis, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMessage(msg);
			}
		});
		timer.setRepeats(false);
		timer.start();
        return true;
    }    

	public void removeMessages(int message) {
		if(timer != null) {
			timer.stop();	
		}
	}
	
    public final void removeCallbacksAndMessages(Object token) {
		if(timer != null) {
			timer.stop();	
		}
    }
}
