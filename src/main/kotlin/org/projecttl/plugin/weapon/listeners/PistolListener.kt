package org.projecttl.plugin.weapon.listeners

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Effect
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Pistol


class PistolListener(private val plugin: WeaponPlugin): Listener {

    private val fixAmmoCount = 12

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val pistol = Pistol(plugin)

        if (player.inventory.itemInMainHand.type == pistol.itemStack().type && player.inventory.itemInMainHand.itemMeta.localizedName == pistol.getItemName()) {
            if (player.inventory.itemInMainHand.itemMeta.customModelData == pistol.getCustomModelData()) {
                when (event.action) {
                    Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                        if (pistol.getAmmo() != 0) {
                            if (!pistol.getShoot()) {
                                pistol.setReload(true)
                                val bullet: Projectile = player.launchProjectile<Projectile>(Snowball::class.java).let { bullet ->
                                    bullet.velocity = player.location.direction.multiply(5)

                                    bullet
                                }

                                bullet.world.playEffect(player.location, Effect.SMOKE, 10)
                                bullet.world.playSound(
                                    bullet.location,
                                    Sound.ENTITY_FIREWORK_ROCKET_BLAST,
                                    100.toFloat(),
                                    1.toFloat()
                                )

                                pistol.setAmmo(pistol.getAmmo() - 1)
                                player.sendActionBar(Component.text("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}${pistol.getAmmo()}/${fixAmmoCount}"))

                                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                                    pistol.setReload(false)
                                }, (0.7 * 20).toLong())
                            }

                        } else {
                            player.sendActionBar(Component.text("${ChatColor.GOLD}Left Bullet: ${ChatColor.RED}${pistol.getAmmo()}/${fixAmmoCount}"))
                            player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                        }
                    }

                    Action.RIGHT_CLICK_AIR -> {
                        player.sendActionBar(Component.text("${ChatColor.GOLD}RELOADING..."))
                        player.playSound(
                            player.location,
                            Sound.BLOCK_IRON_DOOR_OPEN,
                            100.toFloat(),
                            2.toFloat()
                        )

                        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                            pistol.setAmmo(fixAmmoCount)
                            player.sendActionBar(Component.text("${ChatColor.GREEN}RELOAD COMPLETE!"))

                            player.playSound(
                                player.location,
                                Sound.BLOCK_IRON_DOOR_CLOSE,
                                100.toFloat(),
                                2.toFloat()
                            )
                        }, (1.5 * 20).toLong())
                    }

                    else -> {
                        // void code
                    }
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damageTarget = event.damager
        val pistol = Pistol(plugin)

        if (damageTarget is Snowball) {
            val bullet: Snowball = event.damager as Snowball

            if (bullet.shooter is Player) {
                val shooter = bullet.shooter as Player

                if (shooter.inventory.itemInMainHand.type == pistol.itemStack().type && shooter.inventory.itemInMainHand.itemMeta.localizedName == pistol.getItemName()) {
                    if (shooter.inventory.itemInMainHand.itemMeta.customModelData == pistol.getCustomModelData()) {
                        event.damage = 5.0
                    }
                }
            }
        }
    }
}