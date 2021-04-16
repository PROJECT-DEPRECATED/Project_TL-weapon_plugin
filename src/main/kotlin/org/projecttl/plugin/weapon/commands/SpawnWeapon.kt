package org.projecttl.plugin.weapon.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Pistol
import org.projecttl.plugin.weapon.utils.Sword

class SpawnWeapon: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        } else {
            if (command.name == "weapon" && sender.name == WeaponPlugin.target && sender.isOp) {
                when {
                    args.isNullOrEmpty() -> {
                        return false
                    }

                    args.size == 1 -> {
                        when (args[0]) {
                            "pistol" -> {
                                Pistol.giveItem(sender)
                                return true
                            }

                            "knife" -> {
                                Sword.giveItem(sender)
                                return true
                            }
                        }
                    }
                }
            }
        }


        return false
    }
}