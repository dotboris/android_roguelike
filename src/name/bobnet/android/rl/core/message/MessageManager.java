/*
 * Class Name:			MessageManager.java
 * Class Purpose:		Handles all messages
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.message;

import java.util.LinkedList;
import java.util.Queue;

public class MessageManager implements Messenger {

	// message singleton
	private static Messenger messenger;

	// variables
	private Queue<Message> messageQueue;

	public MessageManager() {
		// create the message queue
		messageQueue = new LinkedList<Message>();

		// set the messenger singleton
		if (messenger == null)
			messenger = this;
	}

	/**
	 * @return the messenger singleton
	 */
	public static Messenger getMessenger() {
		return messenger;
	}

	@Override
	public void sendMessage(Message m) {
		if (m == null)
			throw new NullPointerException("message cannot be null");

		// add message
		messageQueue.add(m);
	}

	/**
	 * Sends all the queued messages
	 */
	public void processMessages() {
		// temp message
		Message m;

		// send messages
		while (!messageQueue.isEmpty()) {
			m = messageQueue.poll();
			m.getReceiver().processMessage(m);
		}

	}
}
