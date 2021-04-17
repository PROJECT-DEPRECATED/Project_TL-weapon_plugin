package org.projecttl.plugin.weapon.listeners

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
import org.bukkit.scheduler.BukkitRunnable
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Pistol


class PistolListener(private var plugin: WeaponPlugin): Listener {

    private val reloading = plugin.weaponConfig().getBoolean("weapon.projecttl.pistol.reload")
    private val leftAmmo = plugin.weaponConfig().getInt("weapon.projecttl.pistol")
    private val fixAmmoCount = 12

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        val playerMainHand = player.inventory.itemInMainHand

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (player.name == WeaponPlugin.target && playerMainHand.type == Pistol.itemStack().type) {
                if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                    when {
                        leftAmmo > 0 -> {
                            shoot(player)
                        }

                        else -> {
                            with(player) {
                                playSound(this.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                                sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.RED}$leftAmmo${ChatColor.GREEN}/$fixAmmoCount")
                            }
                        }
                    }
                }
            }
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.name == WeaponPlugin.target && playerMainHand.type == Pistol.itemStack().type) {
                if (playerMainHand.itemMeta.displayName == Pistol.getItemName() && playerMainHand.itemMeta.customModelData == Pistol.getCustomModelData()) {
                    when {
                        leftAmmo > 0 -> {
                            with(player) {
                                sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}$leftAmmo/4")
                                playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 100.toFloat(), 2.toFloat())
                            }
                        }

                        else -> {
                            reload(player)
                        }
                    }
                }
            }
        }
    }

    // When the player is hit by a bullet
    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.damager is Snowball) {
            val projectile = event.damager as Projectile
            val entityId: Int = event.entity.entityId

            val bullet = plugin.bullets

            if (projectile.shooter is Player) {
                if (bullet.contains(entityId)) {
                    bullet.remove(entityId)
                    event.damage = event.damage + 5
                }
            }
        }
    }

    private fun shoot(player: Player) {
        val bullet: Projectile = player.launchProjectile(Snowball::class.java).let { bullet ->
            bullet.velocity = player.location.direction.multiply(5)
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

        plugin.bullets.add(bullet.entityId)
        plugin.weaponConfig().set("weapon.projecttl.pistol", PistolListener(plugin).leftAmmo - 1)
        player.sendActionBar("${ChatColor.GOLD}Left Bullet: ${ChatColor.GREEN}${PistolListener(plugin).leftAmmo}/${PistolListener(plugin).fixAmmoCount}")
    }

    private fun reload(player: Player) {
        with(player) {
            when {
                !PistolListener(plugin).reloading -> {
                    sendActionBar("${ChatColor.GOLD}Reloading...")
                    playSound(
                        this.location,
                        Sound.BLOCK_IRON_DOOR_OPEN,
                        100.toFloat(),
                        2.toFloat()
                    )

                    plugin.weaponConfig().set("weapon.projecttl.pistol.reload", true)

                    inventory.itemInOffHand.subtract(1)
                    object : BukkitRunnable() {
                        override fun run() {
                            playSound(
                                player.location,
                                Sound.BLOCK_IRON_DOOR_CLOSE,
                                100.toFloat(),
                                2.toFloat()
                            )

                            plugin.weaponConfig().set("weapon.projecttl.pistol", fixAmmoCount)
                            plugin.weaponConfig().set("weapon.projecttl.pistol.reload", false)
                        }
                    }.runTaskLater(plugin, (1 * 20).toLong())
                }

                else -> {
                    sendMessage("Rocket_Launcher> ${ChatColor.GOLD}You're already reloading!")
                    playSound(
                        this.location,
                        Sound.ENTITY_ENDERMAN_TELEPORT,
                        100.toFloat(),
                        1.0.toFloat()
                    )
                }
            }
        }
    }
}