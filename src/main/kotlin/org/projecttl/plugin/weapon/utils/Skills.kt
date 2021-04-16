package org.projecttl.plugin.weapon.utils

import org.bukkit.ChatColor
import org.bukkit.Effect
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.projecttl.plugin.weapon.WeaponPlugin

class Skills(private val plugin: WeaponPlugin) {


    private var coolDown = false
    private var coolTime = 30 * 20

    fun powerBooster(player: Player) {
        if (!coolDown) {
            coolDown = true

            player.run {
                setCooldown(Sword.itemStack().type, 0)
                playEffect(location, Effect.SMOKE, 10)
            }

            player.addPotionEffects(
                mutableListOf(
                    PotionEffect(PotionEffectType.SPEED, coolTime, 5, true),
                    PotionEffect(PotionEffectType.JUMP, coolTime, 3, true),
                    PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, coolTime, 10, true),
                    PotionEffect(PotionEffectType.NIGHT_VISION, coolTime, 1, true),
                )
            )

            object : BukkitRunnable() {
                override fun run() {
                    coolDown = false
                    player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You're cooldown is end!")
                }
            }.runTaskLater(plugin, (120 * 20).toLong())

        } else {
            player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You can use the skill 2 minutes from the time you use it.")
        }
    }
}