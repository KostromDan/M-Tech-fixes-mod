package dev.kostromdan.mods.mtech_fixes_mod.mixins;

import dev.kostromdan.mods.mtech_fixes_mod.Config;
import dev.kostromdan.mods.mtech_fixes_mod.utils.PlayerUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shadows.apotheosis.ench.enchantments.masterwork.EarthsBoonEnchant;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.Apoth;

@Mixin(EarthsBoonEnchant.class)
public class EarthsBoonEnchantMixin {

    /**
     * @author KostromDan
     * @reason Adjust chance separately for fake players and real players to balance EarthsBoonEnchant.
     */
    @Overwrite
    public void provideBenefits(BreakEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = player.getMainHandItem();
        int level = stack.getEnchantmentLevel((EarthsBoonEnchant) (Object) this);
        if (player.level.isClientSide) return;

        double chance;
        if (PlayerUtils.isFakePlayer(player)){
            chance = Config.earthsBoonFakePLayer;
        }else {
            chance = Config.earthsBoonRealPLayer;
        }

        if (e.getState().is(Tags.Blocks.STONE) && level > 0 && player.random.nextFloat() <= chance * level) {
            ItemStack newDrop = new ItemStack(ForgeRegistries.ITEMS.tags().getTag(Apoth.Tags.BOON_DROPS).getRandomElement(player.random).orElse(Items.AIR));
            Block.popResource(player.level, e.getPos(), newDrop);
        }
    }
}
