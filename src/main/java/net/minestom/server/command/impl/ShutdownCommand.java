package net.minestom.server.command.impl;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A simple shutdown command.
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown");
        this.addSyntax(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext commandContext) {
        if (sender instanceof Player player && player.getPermissionLevel() < 4) {
            // TODO: Get rank name from a rank object.
            I18n.of(player, "Admin").message(constant -> constant.command.requiresRank);
            return;
        }

        MinecraftServer.stopCleanly();
    }
}
