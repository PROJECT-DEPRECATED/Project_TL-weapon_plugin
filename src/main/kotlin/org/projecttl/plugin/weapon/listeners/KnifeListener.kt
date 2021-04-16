package org.projecttl.plugin.weapon.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Skills
import org.projecttl.plugin.weapon.utils.Sword

class KnifeListener(private val plugin: WeaponPlugin): Listener {

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        val mainHand = player.inventory.itemInMainHand

        val skills = Skills(plugin)

        if (action == Action.RIGHT_CLICK_AIR && action == Action.RIGHT_CLICK_BLOCK) {
            if (player.name == WeaponPlugin.target && mainHand.type == Sword.itemStack().type) {
                if (mainHand.itemMeta.displayName == Sword.itemName() && mainHand.itemMeta.customModelData == Sword.customModelData()) {
                    skills.powerBooster(player)
                }
            }
        }
    }
}