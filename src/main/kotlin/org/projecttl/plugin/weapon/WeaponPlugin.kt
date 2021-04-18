package org.projecttl.plugin.weapon

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.projecttl.plugin.weapon.commands.SpawnWeapon
import org.projecttl.plugin.weapon.commands.arguments.ArgumentSpawnWeapon
import org.projecttl.plugin.weapon.listeners.KnifeListener
import org.projecttl.plugin.weapon.listeners.PistolListener
import java.io.File

class WeaponPlugin: JavaPlugin() {

    companion object {
        var target = "Project_TL"
    }

    private var getFile: File? = null
    private var configuration: FileConfiguration? = null
    private var manager = server.pluginManager

    override fun onEnable() {
        load()
        logger.info("Plugin enabled!")

        getCommand("weapon")?.also {
            it.setExecutor(SpawnWeapon(this))
            it.tabCompleter = ArgumentSpawnWeapon()
        }

        manager.registerEvents(PistolListener(this), this)
        manager.registerEvents(KnifeListener(this), this)
    }

    override fun onDisable() {
        save()
        logger.info("Plugin disabled!")
    }

    private fun setDefault() {
        weaponConfig().set("weapon.cooldown", false)
        weaponConfig().set("weapon.projecttl.pistol.reload", false)
        weaponConfig().set("weapon.projecttl.pistol.leftAmmo", 12)

        weaponConfig().set("weapon.cooltime", 60)
        weaponConfig().set("weapon.duration", 15)

        println("System load complete!") // DEBUG_CODE
    }

    private fun load() {
        getFile = File(dataFolder, "config.yml").also {
            if (!it.exists()) {
                configuration?.save(it)
                setDefault()
            }

            configuration?.load(it)
        }
        configuration = YamlConfiguration.loadConfiguration(getFile!!)
    }

    private fun save() {
        configuration?.save(getFile!!)
    }

    fun weaponConfig(): FileConfiguration {
        return configuration!!
    }
}