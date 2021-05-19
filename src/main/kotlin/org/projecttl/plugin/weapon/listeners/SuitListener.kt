package org.projecttl.plugin.weapon.listeners

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Skills
import org.projecttl.plugin.weapon.utils.SuitScanner

class SuitListener(private val plugin: WeaponPlugin): Listener {

    @EventHandler
    fun onEvent(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        val skills = Skills(plugin)

        /* Check Chest Plate START */

        if (player.inventory.chestplate?.type != SuitScanner().itemStack().type) {
            return
        } else if (player.inventory.chestplate?.itemMeta?.displayName() != Component.text(SuitScanner().itemName())) {
            return
        } else if (player.inventory.chestplate?.itemMeta?.lore() != SuitScanner().itemLore()) {
            return
        } else if (player.inventory.chestplate?.itemMeta?.customModelData != SuitScanner().customModelData()) {
            return
        }

        /* Check Chest Plate END */
        
        else {
            if (player.name == WeaponPlugin.target) {
                if (action == Action.RIGHT_CLICK_AIR && player.inventory.itemInMainHand.type == Material.CLOCK) {
                    skills.findPlayer(player, 20)
                }
            }
        }
    }
}
