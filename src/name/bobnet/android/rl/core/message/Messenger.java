package name.bobnet.android.rl.core.message;
/**
 * Messenger interface used to send messages
 * 
 * @author boris
 */
public interface Messenger {
	
	/**
	 * @param m
	 *            the message to be sent
	 * @throws NullPointerException
	 *             thrown when m is null
	 */
	public void sendMessage(Message m);
}
