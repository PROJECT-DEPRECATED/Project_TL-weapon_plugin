package org.projecttl.plugin.weapon.utils

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.projecttl.plugin.weapon.WeaponPlugin

class Pistol(private val plugin: WeaponPlugin) {
    private val itemType: ItemStack = ItemStack(Material.IRON_HOE)
    private val itemName = "${ChatColor.YELLOW}Project_TL's Pistol"
    private val itemLore = listOf<Component>(Component.text("${ChatColor.GREEN}This is Project_TL's Pistol"))
    private val customModelData = 1044

    /* Start path section */

    val path = "weapon.projecttl.pistol"
    val ammoPath = plugin.weaponConfig().getInt("$path.leftAmmo")
    val shooting = plugin.weaponConfig().getBoolean("$path.shooting")
    val reloading = plugin.weaponConfig().getBoolean("$path.reloading")

    /* End path section */

    fun giveItem(player: Player) {
        val pistolMeta = itemType.itemMeta.let {
            it.setLocalizedName(Component.text(itemName).toString())
            it.lore(itemLore)

            it.setCustomModelData(customModelData)

            it
        }

        itemType.itemMeta = pistolMeta
        player.inventory.addItem(itemType)
    }

    fun itemStack(): ItemStack {
        return itemType
    }

    fun getItemName(): String {
        return itemName
    }

    fun getItemLore(): List<Component> {
        return itemLore
    }

    fun getCustomModelData(): Int {
        return customModelData
    }

    /* Start config */

    fun getAmmo(): Int {
        return ammoPath
    }

    fun setAmmo(count: Int) {
        plugin.weaponConfig().set("$path.leftAmmo", count)
    }

    fun getShoot(): Boolean {
        return shooting
    }

    fun setShoot(status: Boolean) {
        plugin.weaponConfig().set("$path.shooting", status)
    }

    fun getReload(): Boolean {
        return reloading
    }

    fun setReload(status: Boolean) {
        plugin.weaponConfig().set("$path.reloading", status)
    }

    /* End config */
}