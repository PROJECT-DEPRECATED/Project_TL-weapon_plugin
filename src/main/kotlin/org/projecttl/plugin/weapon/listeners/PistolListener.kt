package org.projecttl.plugin.weapon.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.utils.Pistol

class PistolListener: Listener {

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        val playerMainHand = player.inventory.itemInMainHand

        if (action == Action.RIGHT_CLICK_AIR && action == Action.RIGHT_CLICK_BLOCK) {
            if (player.name == "Project_TL") {
                if (playerMainHand.type == Pistol.itemStack().type) {
                    if (playerMainHand.itemMeta.displayName == Pistol.getItemName()) {
                    }
                }
            }
        }
    }
}