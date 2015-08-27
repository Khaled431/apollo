package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ThirdItemActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ThirdItemActionMessage}.
 *
 * @author Graham
 */
public final class ThirdItemActionMessageDecoder extends MessageDecoder<ThirdItemActionMessage> {

	@Override
	public ThirdItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new ThirdItemActionMessage(interfaceId, id, slot);
	}

}