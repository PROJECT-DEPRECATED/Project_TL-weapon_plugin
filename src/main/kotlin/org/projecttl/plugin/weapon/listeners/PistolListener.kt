package org.projecttl.plugin.weapon.listeners

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

    private val shootingPath = "weapon.projecttl.pistol.shooting"
    private val shooting = plugin.weaponConfig().getBoolean(shootingPath)

    private val reloadingPath = "weapon.projecttl.pistol.reload"
    private val reloading = plugin.weaponConfig().getBoolean(reloadingPath)

    private val leftAmmoPath = "weapon.projecttl.pistol.leftAmmo"
    private val leftAmmo = plugin.weaponConfig().getInt(leftAmmoPath)

    private val fixAmmoCount = 12

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player

        when (event.action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                if (leftAmmo != 0) {
                    if (!shooting) {
                        plugin.weaponConfig().set(shootingPath, true)
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

                        plugin.weaponConfig().set(leftAmmoPath, leftAmmo - 1)
                        player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}${leftAmmo}/${fixAmmoCount}")

                        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                            plugin.weaponConfig().set(shootingPath, false)
                        }, (0.7 * 20).toLong())
                    }

                }
            }

            Action.RIGHT_CLICK_AIR -> {
                plugin.weaponConfig().set(leftAmmoPath, fixAmmoCount)
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damageTarget = event.damager

        if (damageTarget is Snowball) {
            val bullet: Snowball = event.damager as Snowball

            if (bullet.shooter is Player) {
                val shooter = bullet.shooter as Player

                if (shooter.inventory.itemInMainHand.type == Pistol.itemStack().type && shooter.inventory.itemInMainHand.itemMeta.displayName == Pistol.getItemName()) {
                    if (shooter.inventory.itemInMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                        event.damage = 5.0
                    }
                }
            }
        }
    }
}