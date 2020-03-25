package org.apollo.game.release.r181.decoders.map.npc;

import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the fifth {@link NpcActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FifthNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int movementType = (int) reader.getSigned(DataType.BYTE);
		int index = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
		return new NpcActionMessage(5, index, movementType);
	}
}