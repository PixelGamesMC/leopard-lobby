package eu.pixelgamesmc.minecraft.lobby

import eu.pixelgamesmc.minecraft.lobby.command.SetLocationCommand
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerInventoryListener
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerVisibilityListener
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerVitalityListener
import eu.pixelgamesmc.minecraft.lobby.listener.WorldListener
import eu.pixelgamesmc.minecraft.servercore.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.utility.PluginUtil
import org.bukkit.plugin.java.JavaPlugin

class Lobby: JavaPlugin() {

    override fun onEnable() {
        ComponentProvider.loadComponents(this)

        PluginUtil.registerCommands(this, SetLocationCommand(this))

        PluginUtil.registerEvents(this, PlayerInventoryListener(this), PlayerVitalityListener(), PlayerVisibilityListener(this), WorldListener())
    }
}