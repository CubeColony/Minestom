package net.minestom.server.command.impl;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author LBuke (Teddeh)
 */
public class KillCommand extends Command {

    public KillCommand() {
        super("kill");

        this.setDefaultExecutor((sender, context) -> {
            I18n.of(sender, context.getCommandName()).message(constant -> constant.command.kill.usage);
        });

        final ArgumentEntity target = ArgumentType.Entity("targets");

        this.addSyntax((sender, context) -> {
            final EntityFinder entityFinder = context.get(target);
            final List<@NotNull Entity> entities = entityFinder.find(sender);

            if (entities.isEmpty()) {
                I18n.of(sender).message(constant -> constant.command.entityNotFound);
                return;
            }

            I18n.of(sender, entities.size()).message(constant -> constant.command.kill.killedEntities);

            entities.forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.kill();
                    return;
                }

                entity.remove();
            });
        }, target);
    }
}
