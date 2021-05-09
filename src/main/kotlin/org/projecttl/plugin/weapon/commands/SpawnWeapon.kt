package org.projecttl.plugin.weapon.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.projecttl.plugin.weapon.WeaponPlugin
import org.projecttl.plugin.weapon.utils.Knife
import org.projecttl.plugin.weapon.utils.SuitScanner
import java.util.*

class SpawnWeapon(private val plugin: WeaponPlugin): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        } else {
            if (command.name == "weapon" && sender.name == WeaponPlugin.target) {
                when {
                    args.isNullOrEmpty() -> {
                        return false
                    }


                    args.size == 1 -> {
                        if (sender.isOp) {
                            when (args[0]) {
                                "knife" -> {
                                    Knife().giveItem(sender)
                                    return true
                                }

                                "scanner" -> {
                                    SuitScanner().giveItem(sender)
                                    return true
                                }
                            }
                        }
                    }

                    args.size == 2 -> {
                        when (args[0]) {
                            "cooldown" -> {
                                if (sender.isOp) {
                                    val time: Int = Integer.parseInt(args[1])
                                    plugin.weaponConfig().set("weapon.cooltime", time)

                                    sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your cooldown time is $time seconds")

                                    return true
                                }
                            }

                            "duration" -> {
                                if (sender.isOp) {
                                    val time: Int = Integer.parseInt(args[1])
                                    plugin.weaponConfig().set("weapon.duration", time)

                                    sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your effect duration time is $time seconds")

                                    return true
                                }
                            }

                            "default" -> {
                                plugin.weaponConfig().set("weapon.cooltime", 60)
                                plugin.weaponConfig().set("weapon.duration", 20)

                                sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your cooldown time is 60 seconds")
                                sender.sendMessage("<Skill_Manager> ${ChatColor.GREEN}Your effect duration time is 20 seconds")

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