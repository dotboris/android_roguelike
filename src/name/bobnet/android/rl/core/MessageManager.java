/*
 * Class Name:			MessageManager.java
 * Class Purpose:		Handles all messages
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

import java.util.LinkedList;
import java.util.Queue;

import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Messenger;

public class MessageManager implements Messenger {

	// message singleton
	private static Messenger messenger;

	// variables
	private Queue<Message> messageQueue;

	private MessageManager() {
		// create the message queue
		messageQueue = new LinkedList<Message>();
	}

	/**
	 * @return the messenger singleton
	 */
	public static Messenger getMessenger() {
		return messenger;
	}
	
	/**
	 * Called by the engine to get the message manager instance
	 * 
	 * @return the message manager instance
	 */
	public static MessageManager getMessageManager() {
		if (messenger == null) {
			// create the manager
			MessageManager m = new MessageManager();
			messenger = m;
			
			// return the manager
			return m;
		}
		
		// return null as someone else has the manager instance
		return null;
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
