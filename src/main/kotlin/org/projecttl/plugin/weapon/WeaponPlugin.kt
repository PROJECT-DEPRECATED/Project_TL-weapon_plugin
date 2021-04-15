package org.projecttl.plugin.weapon

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class WeaponPlugin: JavaPlugin() {

    private var getFile: File? = null
    private var configuration: FileConfiguration? = null

    override fun onEnable() {
        load()
        logger.info("Plugin enabled!")
    }

    override fun onDisable() {
        save()
        logger.info("Plugin disabled!")
    }

    private fun load() {
        getFile = File("plugin.yml").also {
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