package me.l2x9.core.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SevJ6
 * made this genius patch while fucking around
 * Modified by 254n_m
 */
public class ProjectileCrash extends ViolationManager implements Listener {
    private final Map<EntityType, Material> itemMap = new HashMap<EntityType, Material>() {{
        put(EntityType.SNOWBALL, Material.SNOW_BALL);
        put(EntityType.FIREBALL, Material.FIREBALL);
        put(EntityType.SMALL_FIREBALL, Material.FIREBALL);
        put(EntityType.THROWN_EXP_BOTTLE, Material.EXP_BOTTLE);
        put(EntityType.WITHER_SKULL, null);
    }};

    public ProjectileCrash() {
        super(1, 5);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!itemMap.containsKey(projectile.getType())) return;
        ProjectileSource shooter = projectile.getShooter();
        MovingObjectPosition landing = traceEntity(projectile);
        if (landing == null || !projectile.getWorld().getChunkAt(landing.a().getX(), landing.a().getZ()).isLoaded()) {
            InventoryHolder holder = null;
            if (shooter instanceof InventoryHolder) holder = (InventoryHolder) shooter;
            if (holder == null && shooter instanceof BlockProjectileSource) {
                BlockState block = ((BlockProjectileSource) shooter).getBlock().getState();
                if (block instanceof InventoryHolder) holder = (InventoryHolder) block;
            }
            if (holder != null) {
                increment(holder.hashCode());
                if (getVLS(holder.hashCode()) >= 3 && holder instanceof Dispenser) {
                    Dispenser dispenser = (Dispenser) holder;
                    TNTPrimed tnt = (TNTPrimed) dispenser.getWorld().spawnEntity(dispenser.getLocation(), EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(0);
                } else if (getVLS(holder.hashCode()) >= 25) {
                    holder.getInventory().remove(itemMap.get(projectile.getType()));
                }
            }
            projectile.remove();
            if (!(projectile instanceof WitherSkull))
                Utils.log("&3Prevented a &r&a%s&r&3 from going into unloaded chunks in %s", projectile.getClass().getSimpleName(), Utils.formatLocation(projectile.getLocation()));
        }
    }

    public MovingObjectPosition traceEntity(Entity entity) {
        World world = ((CraftWorld) entity.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        Location loc = entity.getLocation();
        MovingObjectPosition landing = null;
        double posX = loc.getX();
        double posY = loc.getY();
        double posZ = loc.getZ();
        BlockPosition originalPosition = new BlockPosition(posX, posY, posZ);
        float gravityModifier = Utils.getGravityModifier(entity);
        float motionModifier = Utils.getMotionModifier(entity);

        Vector velocity = new Vector(nmsEntity.motX, nmsEntity.motY, nmsEntity.motZ);
        if (nmsEntity instanceof EntityFireball) {
            EntityFireball fireball = (EntityFireball) nmsEntity;
            velocity = new Vector(fireball.dirX, fireball.dirY, fireball.dirZ);
        }
        double motionX = velocity.getX();
        double motionY = velocity.getY();
        double motionZ = velocity.getZ();

        boolean hasLanded = false;
        while (!hasLanded && posY > 0.0D) {
            double fPosX = posX + motionX;
            double fPosY = posY + motionY;
            double fPosZ = posZ + motionZ;

            Vec3D start = new Vec3D(posX, posY, posZ);
            Vec3D future = new Vec3D(fPosX, fPosY, fPosZ);

            landing = world.rayTrace(start, future);
            hasLanded = (landing != null) && (landing.a() != null);

            posX = fPosX;
            posY = fPosY;
            posZ = fPosZ;
            motionX *= motionModifier;
            motionY *= motionModifier;
            motionZ *= motionModifier;
            motionY -= gravityModifier;

            double distSquared = originalPosition.distanceSquared(posX, posY, posZ);
            if (distSquared > 48000) break;
        }
        return landing;
    }
}