package com.ocdsoft.bacta.swg.cu.message.game.server;

import com.ocdsoft.bacta.soe.io.udp.game.GameServerState;
import com.ocdsoft.bacta.soe.message.GameNetworkMessage;
import com.ocdsoft.bacta.soe.util.SOECRC32;
import com.ocdsoft.bacta.swg.cu.object.login.ClusterEntry;
import lombok.Getter;

import java.nio.ByteBuffer;

public class GameServerStatus extends GameNetworkMessage {

    private static final short priority = 0x4;
    private static final int messageType = SOECRC32.hashCode(GameServerStatus.class.getSimpleName());

    @Getter
    private final ClusterEntry clusterEntry;

    public GameServerStatus(GameServerState<ClusterEntry> gameServerState) {
        super(priority, messageType);

        this.clusterEntry = gameServerState.getClusterEntry();
	}

    public GameServerStatus(ByteBuffer buffer) {
        super(priority, messageType);

        this.clusterEntry = new ClusterEntry(buffer);
    }

    @Override
    public void writeToBuffer(ByteBuffer buffer) {
        clusterEntry.writeToBuffer(buffer);
    }
}
