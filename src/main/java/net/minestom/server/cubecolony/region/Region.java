package net.minestom.server.cubecolony.region;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
@Entity
@Table(name = "regions")
public class Region {

    @Id
    private long id;

    @Column(name = "type", nullable = false)
    private RegionType type;

    @Column(name = "right_x", nullable = false)
    private double rightX;

    @Column(name = "right_y", nullable = false)
    private double rightY;

    @Column(name = "right_z", nullable = false)
    private double rightZ;

    @Column(name = "left_x", nullable = false)
    private double leftX;

    @Column(name = "left_y", nullable = false)
    private double leftY;

    @Column(name = "left_z", nullable = false)
    private double leftZ;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_connection")
    private List<Region> children;


    public Region(RegionType type, BoundingBox bb) {
        this.type = type;
        this.rightX = bb.minX();
        this.rightY = bb.minY();
        this.rightZ = bb.minZ();
        this.leftX = bb.maxX();
        this.leftY = bb.maxY();
        this.leftZ = bb.maxZ();
    }

    public Region() {
    }

    public long getId() {
        return id;
    }

    public RegionType getType() {
        return type;
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox((int) rightX, (int) rightY, (int) rightZ, (int) leftX, (int) leftY, (int) leftZ);
    }

    public List<Region> getChildren() {
        return children;
    }

    public boolean hasNodes() {
        return Objects.nonNull(this.children) && !this.children.isEmpty();
    }
}
