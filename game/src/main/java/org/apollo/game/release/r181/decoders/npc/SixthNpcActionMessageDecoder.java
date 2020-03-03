package org.apollo.game.release.r181.decoders.npc;

import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the sixth {@link NpcActionMessage}.
 *
 * @author Stuart
 */
public final class SixthNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
		return new NpcActionMessage(6, index, 0);
	}
}