package com.asmack;

import org.jivesoftware.smack.packet.Message;

/*
 * version����OnContactStateListener��һ���¼��ӿڣ��������£�
 */
public interface OnMessageListener {
	/*
	 * @return ������յ�����Ϣ
	 */
	public void processMessage(Message message);
}
