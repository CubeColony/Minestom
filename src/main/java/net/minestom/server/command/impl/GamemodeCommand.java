package net.minestom.server.command.impl;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Command that make a player change gamemode, made in the style of the vanilla /gamemode command.
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Commands/gamemode">...</a>
 */
public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode", "gm");

        //Upon invalid usage, print the correct usage of the command to the sender
        this.setDefaultExecutor((sender, context) -> {
            I18n.of(sender, context.getCommandName()).message(constant -> constant.command.gamemode.usage);
        });

        //GameMode parameter
        final ArgumentEnum<GameMode> gamemodeArgument = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);
        final ArgumentEntity targetArgument = ArgumentType.Entity("targets").onlyPlayers(true);

        gamemodeArgument.setCallback((sender, exception) -> {
            I18n.of(sender, exception.getInput()).message(constant -> constant.command.gamemode.invalidGamemode);
        });

        this.addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                I18n.of(sender).message(constant -> constant.command.playerCommandOnly);
                return;
            }

            /*
            if (!Role.hasRole(player, "admin")) {
                I18n.of(player, "Admin").message(constant -> constant.command.requiresRank);
                return;
            }
             */

            final GameMode mode = context.get(gamemodeArgument);
            this.executeSelf(player, mode);
        }, gamemodeArgument);

        this.addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                /*
                if (!Role.hasRole(player, "admin")) {
                    I18n.of(player, "Admin").message(constant -> constant.command.requiresRank);
                    return;
                }
                 */
            }

            final EntityFinder finder = context.get(targetArgument);
            final GameMode mode = context.get(gamemodeArgument);

            this.executeOthers(sender, mode, finder.find(sender));
        }, gamemodeArgument, targetArgument);
    }

    /**
     * Sets the gamemode for the specified entities, and notifies them (and the sender) in the chat.
     */
    private void executeOthers(@NotNull CommandSender sender, @NotNull GameMode mode, @NotNull List<Entity> entities) {
        if (entities.isEmpty()) {
            I18n.of(sender).message(constant -> constant.command.entityNotFound);
            return;
        }

        for (Entity entity : entities) {
            if (!(entity instanceof Player target)) {
                continue;
            }

            if (target.equals(sender)) {
                this.executeSelf(target, mode);
                continue;
            }

            target.setGameMode(mode);

            //Send a message to the changed player and the sender
            I18n.of(target, this.getGamemodeName(target, mode)).message(constant -> constant.command.gamemode.gamemodeChanged);
            I18n.of(sender, target.getUsername(), this.getGamemodeName(sender, mode)).message(constant -> constant.command.gamemode.gamemodeSuccessOther);
        }
    }

    /**
     * Sets the gamemode for the executing Player, and notifies them in the chat.
     */
    private void executeSelf(@NotNull Player sender, @NotNull GameMode mode) {
        sender.setGameMode(mode);

        //Send the translated message to the player.
        I18n.of(sender, this.getGamemodeName(sender, mode)).message(constant -> constant.command.gamemode.gamemodeSuccessSelf);
    }

    private @NotNull String getGamemodeName(@NotNull CommandSender sender, @NotNull GameMode mode) {
        return switch (mode) {
            case SURVIVAL -> I18n.of(sender).asString(constant -> constant.command.gamemode.survival);
            case CREATIVE -> I18n.of(sender).asString(constant -> constant.command.gamemode.creative);
            case ADVENTURE -> I18n.of(sender).asString(constant -> constant.command.gamemode.adventure);
            case SPECTATOR -> I18n.of(sender).asString(constant -> constant.command.gamemode.spectator);
        };
    }
}
