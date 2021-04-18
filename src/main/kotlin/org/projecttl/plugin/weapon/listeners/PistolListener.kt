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


class PistolListener(private var plugin: WeaponPlugin): Listener {

    private val reloading = plugin.weaponConfig().getBoolean("weapon.projecttl.pistol.reload")
    private val leftAmmo = plugin.weaponConfig().getInt("weapon.projecttl.pistol.leftAmmo")
    private val fixAmmoCount = 12

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        val playerMainHand = player.inventory.itemInMainHand

        when (action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                if (player.name == WeaponPlugin.target && playerMainHand.type == Pistol.itemStack().type) {
                    if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                        when {
                            leftAmmo > 0 -> {
                                shoot(player)
                            }

                            else -> {
                                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                                player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.RED}$leftAmmo${ChatColor.GREEN}/$fixAmmoCount")
                            }
                        }
                    }
                }
            }

            Action.RIGHT_CLICK_AIR -> {
                if (player.name == WeaponPlugin.target && playerMainHand.type == Pistol.itemStack().type) {
                    if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                        when (leftAmmo) {
                            fixAmmoCount -> {
                                player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}$leftAmmo/$fixAmmoCount")
                                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                            }
                            else -> {
                                reload(player)
                            }
                        }
                    }
                }
            }

            Action.RIGHT_CLICK_BLOCK -> {
                if (player.name == WeaponPlugin.target && playerMainHand.type == Pistol.itemStack().type) {
                    if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                        when {
                            leftAmmo == fixAmmoCount -> {
                                player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}$leftAmmo/$fixAmmoCount")
                                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                            }

                            leftAmmo <= 1 -> {
                                player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GOLD}$leftAmmo/$fixAmmoCount")
                                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                            }

                            else -> {
                                player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.RED}$leftAmmo/$fixAmmoCount")
                                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                            }
                        }
                    }

                    event.isCancelled = true
                }
            }
            else -> {
                // void
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

    private fun shoot(player: Player) {
        val bullet: Projectile = player.launchProjectile(Snowball::class.java).let { bullet ->
            bullet.velocity = player.location.direction.multiply(5)
            bullet
        }

        bullet.world.playEffect(bullet.location, Effect.SMOKE, 10)
        bullet.world.playSound(
            player.location,
            Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST,
            100.toFloat(),
            1.toFloat()
        )

        plugin.weaponConfig().set("weapon.projecttl.pistol.leftAmmo", leftAmmo - 1)
        player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}${leftAmmo}/${fixAmmoCount}")
    }

    private fun reload(player: Player) {
        when {
            !reloading -> {
                player.sendActionBar("${ChatColor.GOLD}Reloading...")
                player.playSound(
                    player.location,
                    Sound.BLOCK_IRON_DOOR_OPEN,
                    100.toFloat(),
                    2.toFloat()
                )

                plugin.weaponConfig().set("weapon.projecttl.pistol.reload", true)

                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    player.playSound(
                        player.location,
                        Sound.BLOCK_IRON_DOOR_CLOSE,
                        100.toFloat(),
                        2.toFloat()
                    )

                    plugin.weaponConfig().set("weapon.projecttl.pistol.leftAmmo", 12)
                    plugin.weaponConfig().set("weapon.projecttl.pistol.reload", false)
                }, (1.5 * 20).toLong())
            }

            else -> {
                player.sendMessage("Rocket_Launcher> ${ChatColor.GOLD}You're already reloading!")
                player.playSound(
                    player.location,
                    Sound.ENTITY_ENDERMAN_TELEPORT,
                    100.toFloat(),
                    1.0.toFloat()
                )
            }
        }
    }
}