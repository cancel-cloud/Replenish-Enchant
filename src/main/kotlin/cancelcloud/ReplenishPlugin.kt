package cancelcloud

import cancelcloud.enchantment.replenish.listener.HarvestListener
import org.bukkit.plugin.java.JavaPlugin

class ReplenishPlugin : JavaPlugin(){
    companion object{
        lateinit var instance: ReplenishPlugin
    }

    override fun onEnable() {
        instance = this

        println("ReplenishEnchant loading...")
        server.pluginManager.registerEvents(
            HarvestListener(this),
            this
        )
    }
}