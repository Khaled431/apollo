package org.apollo.game.release.r181;

import org.apollo.game.message.impl.RemoveFriendMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link RemoveFriendMessage}.
 *
 * @author Major
 */
public final class RemoveFriendMessageDecoder extends MessageDecoder<RemoveFriendMessage> {

	@Override
	public RemoveFriendMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = reader.getString();
		return new RemoveFriendMessage(username);
	}

}