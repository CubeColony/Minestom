package net.minestom.demo.commands;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentComponent;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.monster.GiantMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.Vector;

public class EchoCommand extends Command {
    public EchoCommand() {
        super("echo");

        this.setDefaultExecutor((sender, context) -> sender.sendMessage(
                Component.text("Usage: /echo <json> [uuid]")
                        .hoverEvent(Component.text("Click to get this command.")
                        .clickEvent(ClickEvent.suggestCommand("/echo ")))));

        ArgumentComponent json = ArgumentType.Component("json");
        ArgumentUUID uuid = ArgumentType.UUID("uuid");

        this.addSyntax((sender, context) -> {
            sender.sendMessage(context.get(json));

            final Pos center = sender.asPlayer().getPosition();

            final LivingEntity entity = new LivingEntity(EntityType.GIANT);
            entity.setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.builder(Material.STONE).build());

            final GiantMeta meta = (GiantMeta) entity.getEntityMeta();

            entity.setInstance(sender.asPlayer().getInstance(), center);

            MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
                final float radius = 4f;
                float angle = 0f;

                float angle2 = 20f;

                @Override
                public void run() {
                    final Pos pos = entity.getPosition();
                    final double x = (this.radius * Math.sin(this.angle));
                    final double z = (this.radius * Math.cos(this.angle));
                    this.angle += 0.1;

                    entity.teleport(pos.withX(center.x() + x).withZ(center.z() + z));

                    final double x2 = (-this.radius * Math.sin(this.angle2));
                    final double z2 = (-this.radius * Math.cos(this.angle2));
                    this.angle2 += 0.1;
                    entity.lookAt(pos.withX(center.x() + x2).withZ(center.z() + z2));
                }
            }).repeat(Duration.ofMillis(50)).schedule();
        }, json);

        this.addSyntax((sender, context) -> {
            sender.sendMessage(Identity.identity(context.get(uuid)), context.get(json), MessageType.CHAT);
        }, uuid, json);
    }

    private Vec getRightHeadDirection(Entity entity) {
        final Vec direction = entity.getPosition().direction().normalize();
        return new Vec(-direction.z(), 0.0, direction.x()).normalize();
    }
}
