package net.minestom.server.command.impl;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

/**
 * @author LBuke (Teddeh)
 */
public class TeleportHereCommand extends Command {

    public TeleportHereCommand() {
        super("tphere");

        final ArgumentEntity target = ArgumentType.Entity("targets");
        this.addSyntax((sender, context) -> {
            final EntityFinder finder = context.get(target);
            final List<Entity> entities = finder.find(sender);

            if (sender instanceof Player player) {
                if (entities.isEmpty()) {
                    I18n.of(player, "player").message(constant -> constant.command.targetNotFound);
                    return;
                }

                final Pos pos = player.getPosition();
                entities.forEach(entity -> entity.teleport(pos));
            }
        }, target);
    }
}
