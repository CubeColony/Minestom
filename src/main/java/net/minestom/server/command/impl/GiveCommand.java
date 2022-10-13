package net.minestom.server.command.impl;

import net.minestom.server.command.builder.Command;
import net.minestom.server.cubecolony.i18n.I18n;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minestom.server.command.builder.arguments.ArgumentType.Integer;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

public class GiveCommand extends Command {

    public GiveCommand() {
        super("give");

        this.setDefaultExecutor((sender, context) -> {
            I18n.of(sender, context.getCommandName()).message(constant -> constant.command.give.usage);
        });

        final var targetArgument = Entity("target").onlyPlayers(true);
        final var itemArgument = ItemStack("item");
        final var countArgument = Integer("count").setDefaultValue(() -> 1);

        this.addSyntax((sender, context) -> {
            final EntityFinder entityFinder = context.get(targetArgument);

            ItemStack itemStack = context.get(itemArgument);

            int count = context.get(countArgument);
            count = Math.min(count, PlayerInventory.INVENTORY_SIZE * 64);

            final List<ItemStack> itemStacks;
            if (count <= 64) {
                itemStack = itemStack.withAmount(count);
                itemStacks = Collections.singletonList(itemStack);
            } else {
                itemStacks = new ArrayList<>();
                while (count > 64) {
                    itemStacks.add(itemStack.withAmount(64));
                    count -= 64;
                }
                itemStacks.add(itemStack.withAmount(count));
            }

            final List<Entity> targets = entityFinder.find(sender);
            for (Entity target : targets) {
                if (target instanceof Player player) {
                    player.getInventory().addItemStacks(itemStacks, TransactionOption.ALL);
                    I18n.of(player, count, itemStack.getMaterial().name()).message(constant -> constant.command.give.receivedItem);
                }
            }

        }, targetArgument, itemArgument, countArgument);

    }
}
