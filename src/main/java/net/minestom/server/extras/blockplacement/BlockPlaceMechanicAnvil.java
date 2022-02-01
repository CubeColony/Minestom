package net.minestom.server.extras.blockplacement;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;

/**
 * @author LBuke (Teddeh)
 */
public class BlockPlaceMechanicAnvil {
    static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        block = event.getBlock();

        final Player player = event.getPlayer();
        final Vec vec = player.getPosition().direction();
        double absX = Math.abs(vec.x());
        double absZ = Math.abs(vec.z());
        if (absX > absZ) {
            if (vec.x() > 0) {
                block = block.withProperty("facing", "east");
            } else {
                block = block.withProperty("facing", "west");
            }
        } else {
            if (vec.z() > 0) {
                block = block.withProperty("facing", "south");
            } else {
                block = block.withProperty("facing", "north");
            }
        }

        event.setBlock(block);
    }
}
