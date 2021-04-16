package org.projecttl.plugin.weapon.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Pistol
import org.projecttl.plugin.weapon.utils.Sword

class SpawnWeapon(private val plugin: WeaponPlugin): CommandExecutor {

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

                    args.size == 2 -> {
                        when (args[0]) {
                            "cooldown" -> {
                                val time: Int = Integer.parseInt(args[1])
                                plugin.weaponConfig().set("weapon.cooltime", args[1])

                                sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your cooldown time is $time seconds")

                                return true
                            }

                            "duration" -> {
                                val time: Int = Integer.parseInt(args[1])
                                plugin.weaponConfig().set("weapon.duration", time)

                                sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your effect duration time is $time seconds")

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