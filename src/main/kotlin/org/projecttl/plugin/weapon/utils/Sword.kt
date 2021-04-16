package org.projecttl.plugin.weapon.utils

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Sword {
    companion object {
        private val itemType: ItemStack = ItemStack(Material.DIAMOND_SWORD)
        private val itemName = "${ChatColor.YELLOW}Project_TL's Knife"
        private val itemLore = listOf("${ChatColor.GREEN}This is Project_TL's Pistol")
        private const val customModelData = 1044

        fun giveItem(player: Player) {
            val meta = itemType.itemMeta.let {
                it.setDisplayName(itemName)
                it.lore = itemLore

                it.setCustomModelData(customModelData)

                it
            }

            itemType.itemMeta = meta
            player.inventory.addItem(itemType)
        }

        fun itemStack(): ItemStack {
            return itemType
        }

        fun itemName(): String {
            return itemName
        }

        fun itemLore(): List<String> {
            return itemLore
        }

        fun customModelData(): Int {
            return customModelData
        }
    }
}