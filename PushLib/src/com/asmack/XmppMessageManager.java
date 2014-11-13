package com.asmack;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class XmppMessageManager implements ChatManagerListener {
	private XMPPConnection _connection;
	private ChatManager manager = null;
	private Context context;

	public void initialize(Context context, XMPPConnection connection) {
		this.context = context;
		_connection = connection;
		manager = _connection.getChatManager();
		manager.addChatListener(this);
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		// TODO Auto-generated method stub
		chat.addMessageListener(new MessageListener() {
			public void processMessage(Chat newchat, Message message) {
				// 若是聊天窗口已存在，将消息转往目前窗口
				// 若是窗口不存在，开新的窗口并注册
				
				System.out
						.println(message.getFrom() + ":" + message.getBody());
				
				if (!ActivityMain.chats.containsKey(message.getFrom())) {
					ActivityMain.chats.put(message.getFrom(), newchat);
				} else {

				}
				
//				Intent intent = new Intent(context, ActivityChat.class);
//				intent.putExtra("user", message.getFrom());
//				context.startActivity(intent);

			}
		});
	}

}
