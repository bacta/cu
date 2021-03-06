package com.ocdsoft.bacta.swg.cu.controller.game.server;

import com.ocdsoft.bacta.soe.GameNetworkMessageController;
import com.ocdsoft.bacta.soe.GameNetworkMessageHandled;
import com.ocdsoft.bacta.soe.RolesAllowed;
import com.ocdsoft.bacta.soe.chat.message.ChatSystemMessage;
import com.ocdsoft.bacta.soe.connection.ConnectionRole;
import com.ocdsoft.bacta.soe.connection.SoeUdpConnection;
import com.ocdsoft.bacta.swg.cu.message.game.client.CmdSceneReady;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GameNetworkMessageHandled(CmdSceneReady.class)
@RolesAllowed({ConnectionRole.AUTHENTICATED})
public class CmdSceneReadyController implements GameNetworkMessageController<CmdSceneReady> {

    private static final Logger logger = LoggerFactory.getLogger(CmdSceneReadyController.class.getSimpleName());

    @Override
    public void handleIncoming(SoeUdpConnection connection, CmdSceneReady message) throws Exception {
        connection.sendMessage(new CmdSceneReady());

        ChatSystemMessage motd = new ChatSystemMessage("Welcome to Bacta!");
        connection.sendMessage(motd);

        logger.debug("Scene Ready for " + connection.getId());
    }
}

