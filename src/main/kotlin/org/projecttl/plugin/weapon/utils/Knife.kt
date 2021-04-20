package org.projecttl.plugin.weapon.utils

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Knife {
    companion object {
        private val itemType: ItemStack = ItemStack(Material.DIAMOND_SWORD)
        private val itemName = "${ChatColor.YELLOW}Project_TL's Knife"
        private val itemLore = listOf<Component>(Component.text("${ChatColor.GREEN}This is Project_TL's Pistol"))
        private const val customModelData = 1044

        fun giveItem(player: Player) {
            val swordMeta = itemType.itemMeta.let {
                it.setLocalizedName(Component.text(itemName).toString())
                it.lore(itemLore)

                it.setCustomModelData(customModelData)

                it
            }

            itemType.itemMeta = swordMeta
            player.inventory.addItem(itemType)
        }

        fun itemStack(): ItemStack {
            return itemType
        }

        fun itemName(): String {
            return itemName
        }

        fun itemLore(): List<Component> {
            return itemLore
        }

        fun customModelData(): Int {
            return customModelData
        }
    }
}