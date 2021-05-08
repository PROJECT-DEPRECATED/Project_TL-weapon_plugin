package org.projecttl.plugin.weapon.utils

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import org.projecttl.plugin.weapon.WeaponPlugin
import java.text.DecimalFormat

class Skills(private val plugin: WeaponPlugin) {

    private var coolDown = plugin.weaponConfig().getBoolean("weapon.cooldown")
    private var duration = plugin.weaponConfig().getInt("weapon.duration") * 20

    private var cooldownTime = plugin.weaponConfig().getInt("weapon.cooltime")

    fun powerBooster(player: Player) {
        if (!coolDown) {
            plugin.weaponConfig().set("weapon.cooldown", true)

            player.run {
                setCooldown(Knife(plugin).itemStack().type, 0)
                playSound(player.location, Sound.ITEM_TOTEM_USE, 100F, 100F)
            }

            player.addPotionEffects(
                mutableListOf(
                    PotionEffect(PotionEffectType.SPEED, duration, 5, true),
                    PotionEffect(PotionEffectType.JUMP, duration, 3, true),
                    PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 10, true),
                    PotionEffect(PotionEffectType.NIGHT_VISION, duration, 1, true),
                    PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 5, true),
                    PotionEffect(PotionEffectType.ABSORPTION, duration, 3, true)
                )
            )

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                plugin.weaponConfig().set("weapon.cooldown", false)
                player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You're cooldown is end!")
            }, (cooldownTime * 20).toLong())


        } else {
            player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You can use the skill $cooldownTime seconds from the time you use it.")
        }
    }

    fun findPlayer(player: Player, duration: Int) {
        val enabled = plugin.weaponConfig().getBoolean("weapon.${player.name}.enabled")

        if (!enabled) {
            plugin.weaponConfig().set("weapon.${player.name}.enabled", true)

            var playerCount = 0
            player.sendMessage("${ChatColor.GOLD}Scanner Result${ChatColor.RESET}:")
            player.sendMessage("============================================")

            val coordinate = DecimalFormat("#.#")

            for (target in plugin.server.onlinePlayers) {
                val getPlayer = Bukkit.getPlayer(target.uniqueId)

                val world = getPlayer?.world
                val x = coordinate.format(getPlayer?.location?.x)
                val y = coordinate.format(getPlayer?.location?.y)
                val z = coordinate.format(getPlayer?.location?.z)

                if (getPlayer?.uniqueId != player.uniqueId) {
                    if (world != player.world) {
                        player.sendMessage("[${ChatColor.RED}INVALID${ChatColor.RESET}] ${ChatColor.YELLOW}${getPlayer?.name} ${ChatColor.RED}is not found this world!")
                        player.sendMessage("============================================")
                        playerCount--
                    } else {
                        player.sendMessage(
                            "[${ChatColor.GREEN}DETECTED${ChatColor.RESET}] ${ChatColor.YELLOW}${getPlayer.name} ${ChatColor.GREEN}is detected."
                        )
                        player.sendMessage(
                            "[${ChatColor.GREEN}X: ${ChatColor.LIGHT_PURPLE}$x${ChatColor.GREEN}," +
                                    " ${ChatColor.GREEN}Y: ${ChatColor.LIGHT_PURPLE}$y${ChatColor.GREEN}," +
                                    " ${ChatColor.GREEN}Z: ${ChatColor.LIGHT_PURPLE}$z${ChatColor.RESET}]"
                        )
                        player.sendMessage("============================================")

                        getPlayer.playSound(getPlayer.location, Sound.BLOCK_CONDUIT_ACTIVATE, 100F, 2F)
                    }

                    getPlayer?.sendActionBar(Component.text("[${ChatColor.RED}CAUTION${ChatColor.RESET}] ${ChatColor.GOLD}You have been detected by the scanner."))
                    getPlayer?.addPotionEffects(
                        mutableListOf(
                            PotionEffect(
                                PotionEffectType.GLOWING,
                                duration * 20,
                                20,
                                true
                            )
                        )
                    )

                    playerCount++
                }
            }

            when {
                playerCount > 1 -> {
                    player.sendActionBar(Component.text("[Scanner] ${ChatColor.GREEN}$playerCount people detected."))
                }

                playerCount == 0 -> {
                    player.sendActionBar(Component.text("[Scanner] ${ChatColor.GOLD}Not detected by the scanner."))
                }

                else -> {
                    player.sendActionBar(Component.text("[Scanner] ${ChatColor.GREEN}$playerCount person detected."))
                }
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                plugin.weaponConfig().set("weapon.${player.name}.enabled", false)
            }, (duration * 5).toLong())
        } else {
            player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You can use the skill 5 seconds from the time you use it.")
        }
    }
}