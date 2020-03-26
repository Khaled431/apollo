package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type If set scroll pos message.
 *
 * @author Khaled Abdeljaber
 */
public class IfSetScrollPosMessage extends Message {

	private final int interfacePacked;
	private final int scrollPosition;

	/**
	 * Instantiates a new If set scroll pos message.
	 *
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param scrollPosition  the scroll position
	 */
	public IfSetScrollPosMessage(int interfaceId, int componentId, int scrollPosition) {
		this.interfacePacked = interfaceId << 16 | componentId;
		this.scrollPosition = scrollPosition;
	}

	/**
	 * Gets interface packed.
	 *
	 * @return the interface packed
	 */
	public int getInterfacePacked() {
		return interfacePacked;
	}

	/**
	 * Gets scroll position.
	 *
	 * @return the scroll position
	 */
	public int getScrollPosition() {
		return scrollPosition;
	}
}
