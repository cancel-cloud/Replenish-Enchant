package cancelcloud.enchantment.replenish.listener

import cancelcloud.enchantment.replenish.ReplenishBootstrap
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.Ageable
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class HarvestListener(private val plugin: JavaPlugin) : Listener {

    // Exakt derselbe Namespace+Key wie im Bootstrapper
    private val replenishKey = NamespacedKey(plugin, ReplenishBootstrap.ENCHANT_NAME)

    // Welche Crops supportest du?
    private val crops = setOf(
        Material.WHEAT,
        Material.CARROTS,
        Material.POTATOES,
        Material.BEETROOTS,
        Material.NETHER_WART,
        Material.SWEET_BERRY_BUSH,
    )

    @Suppress("DEPRECATION")
    private fun getReplenishEnchant(): Enchantment? =
        Enchantment.getByKey(replenishKey)

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        
        // Check for Creative Mode
        if (player.gameMode == org.bukkit.GameMode.CREATIVE) {
            val tool = player.inventory.itemInMainHand
            val enchant = getReplenishEnchant()
            val level = enchant?.let { tool.getEnchantmentLevel(it) } ?: 0
            
            // Only send message if they are actually trying to use the enchant
            if (level > 0 && event.block.type in crops) {
                 net.kyori.adventure.text.Component.text("Replenish does not work in Creative mode")
                     .color(net.kyori.adventure.text.format.NamedTextColor.RED)
                     .let { player.sendActionBar(it) }
            }
            return
        }

        val tool = player.inventory.itemInMainHand
        val enchant = getReplenishEnchant()
        val level = enchant?.let { tool.getEnchantmentLevel(it) } ?: 0
        if (level <= 0) {
            return
        }

        val block = event.block
        val data = block.blockData as? Ageable ?: return
        if (data.age < data.maximumAge) {
            event.isCancelled = true
            return
        }
        if (block.type !in crops) return

        // --- Replanting ---
        event.isDropItems = false
        val origType = block.type
        val drops = block.getDrops(tool)
        val dropLoc = block.location.add(0.5, 0.5, 0.5)

        // Items auswerfen
        for (drop in drops) {
            val spawned = block.world.dropItem(dropLoc, ItemStack(drop.type, drop.amount))
            spawned.velocity = Vector(0.0, 0.1, 0.0)
        }

        // Neu pflanzen im nächsten Tick
        Bukkit.getScheduler().runTask(plugin, Runnable {
            val b = block.world.getBlockAt(block.location)
            b.type = origType
            (b.blockData as Ageable).also { it.age = 0 }
                .let { b.blockData = it }
            val spawnLocation = b.location.add(0.5, 0.1, 0.5)
            plugin.server.getWorld(b.world.name)?.spawnParticle(
                Particle.HAPPY_VILLAGER,   // passender für “Ernte/Anbau”-Stimmung
                spawnLocation,
                15,                        // Anzahl
                0.4,                       // Offset X (bis ±0.4 um die Mitte)
                0.1,                       // Offset Y (nur im unteren 10 % des Blocks)
                0.4,                       // Offset Z (bis ±0.4 um die Mitte)
                0.0                        // extra Speed
            )
        })

        // In Paper: berücksichtigt Unbreaking, Mending etc.
        val result = tool.damage(1, player)
        // Setze das ggf. zurück (falls gebrochen)
        player.inventory.setItemInMainHand(result)
    }
}
