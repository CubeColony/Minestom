package net.minestom.server.cubecolony.authentification.chain;

import com.cubecolony.api.authentification.chain.LoginChainExecutor;
import net.minestom.server.cubecolony.authentification.AuthenticationService;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class LoggingChainExecutor implements LoginChainExecutor {

    private final LoginChainExecutor loginChainExecutor;

    public LoggingChainExecutor(LoginChainExecutor loginChainExecutor) {
        this.loginChainExecutor = loginChainExecutor;
    }

    @Override
    public void execute(UUID uuid, String name, Consumer<String> errorHandler) {
        long was = currentTime();
        try {
            loginChainExecutor.execute(uuid, name, errorHandler);
        } finally {
            AuthenticationService.LOGGER.info("Processed player {} in {} ms", name, currentTime() - was);
        }
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}