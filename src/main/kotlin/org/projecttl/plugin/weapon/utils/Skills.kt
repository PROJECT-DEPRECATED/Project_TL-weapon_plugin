package org.projecttl.plugin.weapon.utils

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.projecttl.plugin.weapon.WeaponPlugin

class Skills(private val plugin: WeaponPlugin) {

    private var coolDown = plugin.weaponConfig().getBoolean("weapon.cooldown")
    private var duration = plugin.weaponConfig().getInt("weapon.duration") * 20

    private var cooldownTime = plugin.weaponConfig().getInt("weapon.cooltime")

    fun powerBooster(player: Player) {
        if (!coolDown) {
            plugin.weaponConfig().set("weapon.cooldown", true)

            player.run {
                setCooldown(Sword.itemStack().type, 0)
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

            object : BukkitRunnable() {
                override fun run() {
                    plugin.weaponConfig().set("weapon.cooldown", false)
                    player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You're cooldown is end!")
                }
            }.runTaskLater(plugin, (cooldownTime * 20).toLong())

        } else {
            player.sendMessage("<Skill_Manager> ${ChatColor.GOLD}You can use the skill $cooldownTime seconds from the time you use it.")
        }
    }
}