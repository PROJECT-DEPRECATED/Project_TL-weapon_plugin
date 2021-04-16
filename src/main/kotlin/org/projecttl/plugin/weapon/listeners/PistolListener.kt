package org.projecttl.plugin.weapon.listeners

import org.bukkit.Effect
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Pistol

class PistolListener(private var plugin: WeaponPlugin): Listener {

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        val playerMainHand = player.inventory.itemInMainHand

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.name == "Project_TL" && playerMainHand.type == Pistol.itemStack().type) {
                if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                    val bullet: Projectile = player.launchProjectile(Snowball::class.java).let { bullet ->
                        bullet.velocity = player.location.direction.multiply(3)
                        bullet
                    }

                    with(bullet) {
                        world.playEffect(bullet.location, Effect.SMOKE, 10)
                        world.playSound(
                            player.location,
                            Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST,
                            100.toFloat(),
                            1.toFloat()
                        )
                    }

                    event.isCancelled = true
                }
            }
        }
    }
}