package org.projecttl.plugin.weapon.commands.arguments

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ArgumentSpawnWeapon: TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Console cannot use this command!")
            throw Exception()
        } else {
            val commandList = mutableListOf<String>()

            if (command.name == "weapon") {
                when (args.size) {
                    1 -> {
                        commandList.add("cooldown")
                        commandList.add("default")
                        commandList.add("duration")
                        commandList.add("knife")

                        return commandList
                    }
                }
            }
        }
        return null
    }
}