package com.ocdsoft.bacta.swg.cu.controller.game.server;

import com.google.inject.Inject;
import com.ocdsoft.bacta.engine.conf.BactaConfiguration;
import com.ocdsoft.bacta.engine.security.authenticator.AccountService;
import com.ocdsoft.bacta.soe.GameNetworkMessageController;
import com.ocdsoft.bacta.soe.GameNetworkMessageHandled;
import com.ocdsoft.bacta.soe.RolesAllowed;
import com.ocdsoft.bacta.soe.connection.ConnectionRole;
import com.ocdsoft.bacta.soe.connection.SoeUdpConnection;
import com.ocdsoft.bacta.soe.object.account.SoeAccount;
import com.ocdsoft.bacta.swg.cu.message.ErrorMessage;
import com.ocdsoft.bacta.swg.cu.message.game.client.ClientIdMsg;
import com.ocdsoft.bacta.swg.cu.message.game.server.ClientPermissionsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GameNetworkMessageHandled(ClientIdMsg.class)
@RolesAllowed({})
public class ClientIdMsgController implements GameNetworkMessageController<ClientIdMsg> {

    private static Logger logger = LoggerFactory.getLogger(ClientIdMsgController.class.getSimpleName());

    private final AccountService<SoeAccount> accountService;
    private final String requiredClientVersion;

    @Inject
    public ClientIdMsgController(final AccountService<SoeAccount> accountService,
                                 final BactaConfiguration configuration) {
        this.accountService = accountService;
        requiredClientVersion = configuration.getString("Bacta/GameServer", "ClientVersion");
    }
    
    @Override
    public void handleIncoming(SoeUdpConnection connection, ClientIdMsg message) throws Exception {

        // Validate client version
        if (!message.getClientVersion().equals(requiredClientVersion)) {
            ErrorMessage error = new ErrorMessage("Login Error", "The client you are attempting to connect with does not match that required by the server.", false);
            connection.sendMessage(error);
            logger.info("Sending Client Error");
            return;
        }

        SoeAccount account = accountService.validateSession(message.getToken());
        if (account == null) {
            ErrorMessage error = new ErrorMessage("Error", "Invalid Session", false);
            connection.sendMessage(error);
            logger.info("Invalid Session: " + message.getToken());
            return;
        }

        connection.setAccountId(account.getId());
        connection.setAccountUsername(account.getUsername());
        connection.addRole(ConnectionRole.AUTHENTICATED);

        // TODO: Actually implement permissions
        ClientPermissionsMessage cpm = new ClientPermissionsMessage(true, true, true, true);
        connection.sendMessage(cpm);
    }
}

