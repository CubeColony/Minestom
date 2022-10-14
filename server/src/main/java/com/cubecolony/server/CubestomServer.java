package com.cubecolony.server;

import com.cubecolony.server.commands.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extras.optifine.OptifineSupport;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 14/10/2022
 */
public class CubestomServer {

    public CubestomServer() {
        MinecraftServer minecraftServer = MinecraftServer.init();

        registerCommands();
        start(minecraftServer);
    }

    private void registerCommands() {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new TestCommand());
        commandManager.register(new EntitySelectorCommand());
        commandManager.register(new HealthCommand());
        commandManager.register(new LegacyCommand());
        commandManager.register(new DimensionCommand());
        commandManager.register(new ShutdownCommand());
        commandManager.register(new TeleportCommand());
        commandManager.register(new PlayersCommand());
        commandManager.register(new FindCommand());
        commandManager.register(new PotionCommand());
        commandManager.register(new TitleCommand());
        commandManager.register(new BookCommand());
        commandManager.register(new ShootCommand());
        commandManager.register(new HorseCommand());
        commandManager.register(new EchoCommand());
        commandManager.register(new SummonCommand());
        commandManager.register(new RemoveCommand());
        commandManager.register(new GiveCommand());
        commandManager.register(new SetBlockCommand());
        commandManager.register(new AutoViewCommand());
        commandManager.register(new SaveCommand());
        commandManager.register(new GamemodeCommand());
        commandManager.register(new ExecuteCommand());
        commandManager.register(new RedirectTestCommand());

        commandManager.setUnknownCommandCallback((sender, command) -> sender.sendMessage(Component.text("Unknown command", NamedTextColor.RED)));
    }

    private void start(MinecraftServer minecraftServer) {
        OptifineSupport.enable();

        minecraftServer.start("0.0.0.0", 25565);
    }
}
