// ReplenishBootstrap.kt
package cancelcloud.enchantment.replenish

import cancelcloud.ReplenishPlugin
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.data.EnchantmentRegistryEntry.EnchantmentCost
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.plugin.java.JavaPlugin

class ReplenishBootstrap : PluginBootstrap {
    companion object {
        const val ENCHANT_NAME = "replenish"
    }

    override fun bootstrap(context: BootstrapContext) {
        // So holst du dir den Plugin-Namen aus plugin.yml:
        val namespace = context.pluginMeta.name.lowercase()  // getPluginMeta().getName() :contentReference[oaicite:0]{index=0}

        context.lifecycleManager.registerEventHandler(
            RegistryEvents.ENCHANTMENT.freeze().newHandler { event ->
                event.registry().register(
                    EnchantmentKeys.create(Key.key(namespace, ENCHANT_NAME)),
                    { b: EnchantmentRegistryEntry.Builder ->
                        b.description(Component.text("Replenish"))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.HOES))
                            .anvilCost(1)
                            .maxLevel(1)
                            .weight(10)
                            .minimumCost(EnchantmentCost.of(1, 0))
                            .maximumCost(EnchantmentCost.of(1, 0))
                            .activeSlots(EquipmentSlotGroup.MAINHAND)
                    }
                )
            }
        )
    }

    override fun createPlugin(context: PluginProviderContext): JavaPlugin =
        ReplenishPlugin()
}
