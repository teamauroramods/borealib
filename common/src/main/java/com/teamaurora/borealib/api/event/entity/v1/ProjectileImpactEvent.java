package com.teamaurora.borealib.api.event.entity.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

/**
 * Fired when a projectile impacts something.
 *
 * @author ebo2022
 * @since
 */
@FunctionalInterface
public interface ProjectileImpactEvent {

    Event<ProjectileImpactEvent> EVENT = Event.createCancellable(ProjectileImpactEvent.class);


    /**
     * Called when a projectile is going to make an impact.
     *
     * @param projectile The projectile that is impacting something
     * @param ray        The {@link HitResult} of the projectile
     * @return Whether the impact should continue being processed
     */
    boolean onProjectileImpact(Projectile projectile, HitResult ray);
}