package com.asmack;

import java.io.File;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class XmppFileManager implements FileTransferListener {

	private XMPPConnection _connection;
	private FileTransferManager _fileTransferManager = null;
	private String answerTo;
	private static File externalFileDir;
	private static File landingDir;
	private Context context;

	private String TAG = "filetransfer";

	private static final String gtalksmsDir = "GTalkSMS";

	public XmppFileManager(Context context) {

		// api level >=8
		externalFileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		landingDir = new File(externalFileDir, gtalksmsDir);

		System.out.println("-----dir:" + landingDir.getAbsolutePath());

		if (!landingDir.exists()) {
			landingDir.mkdirs();
		}
		
		this.context = context;
	}

	public void initialize(final XMPPConnection connection) {
		_connection = connection;
		// important: you have to make a dummy service discovery manager.
		// new ServiceDiscoveryManager(ActivityLoginAndChat.connection);
		// now this line does not cause any problems.
		FileTransferNegotiator.setServiceEnabled(connection, true);
		_fileTransferManager = new FileTransferManager(_connection);
		_fileTransferManager.addFileTransferListener(this);
		
		
		
//		Thread thread = new Thread() {  
//            public void run() {  
////                ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(connection);  
////                if (sdm == null)
////                {
////                    sdm = new ServiceDiscoveryManager(connection);
////                }
////                sdm.addFeature("http://jabber.org/protocol/disco#info");  
////                sdm.addFeature("jabber:iq:privacy");  
//                // Create the file transfer manager  
//                final FileTransferManager managerListner = new FileTransferManager(connection);
//                FileTransferNegotiator.setServiceEnabled(connection, true);  
//                Log.i("File transfere manager", "created");  
//                // Create the listener  
//                managerListner.addFileTransferListener(new FileTransferListener() {  
//                    @Override  
//                    public void fileTransferRequest(final FileTransferRequest request) {  
//                        Log.i("Recieve File","new file transfere request  new file transfere request   new file transfere request");  
//                        Log.i("file request","from" + request.getRequestor());  
//                        
//                        File file;
//                        IncomingFileTransfer transfer = request.accept();  
//                        
//                        Log.i("Recieve File alert dialog", "accepted");  
////                        File file = new File("/sdcard/data/"+ request.getFileName());
//                		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                			send("External Media not mounted read/write");
//                			return;
//                		} else if (!landingDir.isDirectory()) {
//                			send("The directory " + landingDir.getAbsolutePath() + " is not a directory");
//                			return;
//                		}
//                		
//                		file = new File(landingDir, request.getFileName());
//                		if (file.exists()) {
//                			send("The file " + file.getAbsolutePath() + " already exists");
//                			file.delete();
//                		}
//                          
//                        try {  
////                            if (!file.exists()){  
////                                Log.i("have no file",file.getPath());  
////                                file.createNewFile();  
////                            }  
//                        	
//                            transfer.recieveFile(file);  
//                            
////                            send("File transfer: " + file.getName() + " - " + transfer.getStatus());
////                			double percents = 0.0;
////                			while (!transfer.isDone()) {
////                				if (transfer.getStatus().equals(Status.in_progress)) {
////                					percents = ((int) (transfer.getProgress() * 10000)) / 100.0;
////                					send("File transfer: " + file.getName() + " - " + percents + "%");
////                				} else if (transfer.getStatus().equals(Status.error)) {
////                					send(returnAndLogError(transfer));
////                					return;
////                				}
////                				Thread.sleep(1000);
////                			}
////                			if (transfer.getStatus().equals(Status.complete)) {
////                				send("File transfer complete. File saved as " + file.getAbsolutePath());
////                			} else {
////                				send(returnAndLogError(transfer));
////                			}
//
//                        }catch (Exception e) {  
//                            e.printStackTrace();  
//                        }  
//                    }  
//                });  
//            }  
//        };  
//        thread.start();  
	}

	public FileTransferManager getFileTransferManager() {
		return _fileTransferManager;
	}

	private void send(String msg) {
		Log.i(TAG, msg);
	}

	@Override
	public void fileTransferRequest(FileTransferRequest request) {
		final File saveTo;
		// set answerTo for replies and send()
		answerTo = request.getRequestor();
		
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			send("External Media not mounted read/write");
			return;
		} else if (!landingDir.isDirectory()) {
			send("The directory " + landingDir.getAbsolutePath() + " is not a directory");
			return;
		}
		
		saveTo = new File(landingDir, request.getFileName());
		if (saveTo.exists()) {
			send("The file " + saveTo.getAbsolutePath() + " already exists");
			saveTo.delete();
		}
		
		final IncomingFileTransfer transfer = request.accept();
		
		send("File transfer: " + saveTo.getName() + " - "+ request.getFileSize() / 1024 + " KB");
		
//		try{
//			transfer.recieveFile(saveTo);
//		}catch(Exception ex){
//			Log.e(TAG, "receiverFile exception occered---" + ex.toString());
//		}
		
		new Thread(){
			public void run(){
				try{
					transfer.recieveFile(saveTo);
				}catch(Exception ex){
					Log.e(TAG, "receiverFile exception occered---" + ex.toString());
				}
			}
		}.start();
		
//		try {
//			transfer.recieveFile(saveTo);
//			send("File transfer: " + saveTo.getName() + " - "
//					+ transfer.getStatus());
//			double percents = 0.0;
//			while (!transfer.isDone()) {
//				if (transfer.getStatus().equals(Status.in_progress)) {
//					percents = ((int) (transfer.getProgress() * 10000)) / 100.0;
//					send("File transfer: " + saveTo.getName() + " - " + percents + "%");
//				} else if (transfer.getStatus().equals(Status.error)) {
//					send(returnAndLogError(transfer));
//					return;
//				}
//				Thread.sleep(1000);
//			}
//			if (transfer.getStatus().equals(Status.complete)) {
//				send("File transfer complete. File saved as "
//						+ saveTo.getAbsolutePath());
//			} else {
//				send(returnAndLogError(transfer));
//			}
//		} catch (Exception ex) {
//			String message = "Cannot receive the file because an error occured during the process."
//					+ ex;
//			Log.e(TAG, message, ex);
//			send(message);
//		}

	}

	public File getLandingDir() {
		return landingDir;
	}

	public String returnAndLogError(FileTransfer transfer) {
		String message = "Cannot process the file because an error occured during the process.";

		if (transfer.getError() != null) {
			message += transfer.getError();
		}
		if (transfer.getException() != null) {
			message += transfer.getException();
		}
		return message;
	}
}






