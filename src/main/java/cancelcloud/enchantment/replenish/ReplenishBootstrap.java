package cancelcloud.enchantment.replenish;

import cancelcloud.ReplenishPlugin;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry.EnchantmentCost;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReplenishBootstrap implements PluginBootstrap {
    public static final String ENCHANT_NAME = "replenish";

    @Override
    public void bootstrap(BootstrapContext context) {
        var namespace = context.getPluginMeta().getName().toLowerCase();
        var key = EnchantmentKeys.create(Key.key(namespace, ENCHANT_NAME));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.compose(), event -> {
            var hoes = event.getOrCreateTag(ItemTypeTagKeys.HOES);
            event.registry().register(key, builder -> builder
                    .description(Component.text("Replenish"))
                    .supportedItems(hoes)
                    .primaryItems(hoes)
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(5)
                    .minimumCost(EnchantmentCost.of(5, 0))
                    .maximumCost(EnchantmentCost.of(55, 0))
                    .activeSlots(EquipmentSlotGroup.MAINHAND));
        });

        // Table reads #in_enchanting_table. Librarians roll #tradeable uniformly.
        context.getLifecycleManager().registerEventHandler(
                LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT),
                event -> {
                    var registrar = event.registrar();
                    registrar.addToTag(EnchantmentTagKeys.IN_ENCHANTING_TABLE, List.of(key));
                    registrar.addToTag(EnchantmentTagKeys.TRADEABLE, List.of(key));
                });
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new ReplenishPlugin();
    }
}
