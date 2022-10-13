package net.minestom.server.command.impl;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.utils.location.RelativeVec;
import net.minestom.server.utils.position.PointUtil;

import java.util.Objects;

/**
 * @author NotJosh_ (Josh)
 */
public final class TeleportCommand extends Command {

    public TeleportCommand() {
        super("tp");

        this.setDefaultExecutor((sender, context) -> {
            I18n.of(sender, context.getCommandName()).message(constant -> constant.command.teleport.usage);
        });

        final ArgumentRelativeVec3 posArgument = ArgumentType.RelativeVec3("position");
        this.addSyntax((sender, context) -> {
            final Player player = (Player) sender;
            final RelativeVec relativeVec = context.get("position");
            final Pos position = player.getPosition().withCoord(relativeVec.from(player));

            player.teleport(position);

            I18n.of(player, PointUtil.asString(position)).message(constant -> constant.command.teleport.teleportedToPosition);
        }, posArgument);

        final ArgumentEntity target = ArgumentType.Entity("targets").onlyPlayers(true);
        this.addSyntax((sender, context) -> {
            final EntityFinder finder = context.get(target);
            final Entity entity = finder.findFirstEntity(sender);

            if (sender instanceof Player player) {
                if (Objects.isNull(entity)) {
                    I18n.of(player).message(constant -> constant.command.entityNotFound);
                    return;
                }

                player.teleport(entity.getPosition());
            }

            String entityName = "Entity";
            if (entity instanceof Player player) {
                entityName = player.getUsername();
            }

            I18n.of(sender, entityName).message(constant -> constant.command.teleport.teleportedToTarget);
        }, target);
    }
}