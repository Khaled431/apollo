package org.apollo.game.release.r181.decoders.map.player.actions;

import org.apollo.game.message.impl.PlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the third {@link PlayerActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class ThirdPlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int movementType = (int) reader.getUnsigned(DataType.BYTE);
		return new PlayerActionMessage(3, index, movementType);
	}

}