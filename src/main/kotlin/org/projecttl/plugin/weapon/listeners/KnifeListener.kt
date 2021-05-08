package org.projecttl.plugin.weapon.listeners

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Skills
import org.projecttl.plugin.weapon.utils.Knife

class KnifeListener(private val plugin: WeaponPlugin): Listener {

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        val mainHand = player.inventory.itemInMainHand

        val skills = Skills(plugin)

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (mainHand.type == Knife(plugin).itemStack().type && mainHand.itemMeta.lore() == Knife(plugin).itemLore()) {
                if (mainHand.itemMeta.displayName() == Component.text(Knife(plugin).itemName()) && mainHand.itemMeta.customModelData == Knife(plugin).customModelData()) {
                    if (!player.isOp) {
                        if (player.name == WeaponPlugin.target) {
                            skills.powerBooster(player)
                        }
                    } else {
                        skills.findPlayer(player, 20)
                    }
                }
            }
        }
    }
}