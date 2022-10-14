package net.minestom.server.instance;

import net.minestom.server.MinecraftServer;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.PropertyUtils;
import net.minestom.server.world.DimensionType;

import java.io.File;
import java.util.UUID;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class PersistentInstance extends InstanceContainer {

    public static final DimensionType OVERWORLD = DimensionType.builder(NamespaceID.from("tulipe:overworld"))
            .ultrawarm(false)
            .natural(true)
            .skylightEnabled(true)
            .ceilingEnabled(false)
            .ambientLight(15.0f)
            .height(384)
            .minY(-64)
            .logicalHeight(384)
            .build();

    private final File instancesFolder = new File(PropertyUtils.getString("minestom.instances.folder", "instances"));
    private final String name;

    public PersistentInstance(String name, DimensionType dimensionType) {
        super(UUID.nameUUIDFromBytes(name.getBytes()), dimensionType);
        this.name = name;
        if (!instancesFolder.exists())
            instancesFolder.mkdirs();
        setChunkLoader(new AnvilLoader(new File(instancesFolder, name).toPath()));
        MinecraftServer.getInstanceManager().registerInstance(this);
    }

    public PersistentInstance(String name) {
        this(name, OVERWORLD);
    }

    public String getName() {
        return name;
    }
}
