package org.projecttl.plugin.weapon

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.projecttl.plugin.weapon.commands.SpawnWeapon
import org.projecttl.plugin.weapon.commands.arguments.ArgumentSpawnWeapon
import org.projecttl.plugin.weapon.listeners.KnifeListener
import org.projecttl.plugin.weapon.listeners.SuitScannerListener
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

        manager.also {
            it.registerEvents(KnifeListener(this), this)
            it.registerEvents(SuitListener(this), this)
        }
    }

    override fun onDisable() {
        save()
        logger.info("Plugin disabled!")
    }

    private fun load() {
        getFile = File(dataFolder, "config.yml").also {
            if (!it.exists()) {
                configuration?.save(it)
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
