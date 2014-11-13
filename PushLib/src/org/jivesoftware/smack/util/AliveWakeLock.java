package org.jivesoftware.smack.util;

import android.content.Context;

public interface AliveWakeLock {
	public void acquireCpuWakeLock();
	public void releaseCpuLock();
	public void onError(String error);
	public Context getContext();
	public void tryUIThread(Runnable runnable);
}
