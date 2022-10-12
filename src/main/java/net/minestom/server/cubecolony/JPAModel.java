package net.minestom.server.cubecolony;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;


/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@MappedSuperclass
public class JPAModel {

    @Id
    protected long id;

    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;

    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;
}