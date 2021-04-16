package org.projecttl.plugin.weapon.utils

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Pistol {
    companion object {
        private val itemType: ItemStack = ItemStack(Material.IRON_HOE)
        private val itemName = "${ChatColor.YELLOW}Project_TL's Pistol"
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

        fun getItemName(): String {
            return itemName
        }

        fun getItemLore(): List<String> {
            return itemLore
        }

        fun getCustomModelData(): Int {
            return customModelData
        }
    }
}