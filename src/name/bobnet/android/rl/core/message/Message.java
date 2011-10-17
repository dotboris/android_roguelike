/*
 * Class Name:			Message.java
 * Class Purpose:		A simple message
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.message;

import java.util.HashMap;
import java.util.Map;

import name.bobnet.android.rl.core.ents.Entity;

public class Message {

	/**
	 * The types of messages that get sent around
	 * 
	 * @author boris
	 */
	public enum MessageType {
		M_ENT_ENTER_TILE, M_ENT_LEAVE_TILE, M_DO_DAMAGE, M_INTERACT, M_DESTROY
	}

	// variables
	private Entity sender;
	private Entity receiver;
	private MessageType messageType;
	private Map<String, Object> arguments;

	/**
	 * @param receiver
	 *            the receiver of the message
	 * @param messageType
	 *            the type of message to be sent
	 */
	public Message(Entity sender, Entity receiver, MessageType messageType) {
		// set sender, receiver and type
		setMessageType(messageType);
		setReceiver(receiver);
		setSender(sender);

		// create arguments map
		arguments = new HashMap<String, Object>();
	}

	/**
	 * @param key
	 *            the name of the argument
	 * @return the value of the argument
	 */
	public Object getArgument(String key) {
		return arguments.get(key);
	}

	/**
	 * @param key
	 *            the name of the argument
	 * @param value
	 *            the value of the argument
	 */
	public void setArgument(String key, Object value) {
		arguments.put(key, value);
	}

	/**
	 * @return the sender
	 */
	public Entity getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(Entity sender) {
		// set the sender
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public Entity getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver
	 *            the receiver to set
	 * @throws NullPointerException
	 *             thrown when receiver is null
	 */
	public void setReceiver(Entity receiver) {
		if (receiver == null)
			throw new NullPointerException("receiver cannot be null");

		// set the reciever
		this.receiver = receiver;
	}

	/**
	 * @return the messageType
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 * @throws NullPointerException
	 *             thrown when messageType is null
	 */
	public void setMessageType(MessageType messageType) {
		if (messageType == null)
			throw new NullPointerException("messageType cannot be null");

		// set the message type
		this.messageType = messageType;
	}

}
