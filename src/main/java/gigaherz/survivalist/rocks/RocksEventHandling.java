package gigaherz.survivalist.rocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import gigaherz.survivalist.ConfigManager;
import gigaherz.survivalist.Survivalist;
import gigaherz.survivalist.misc.OreDictionaryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class RocksEventHandling
{
    private final Random rnd = new Random();

    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new RocksEventHandling());
    }

    @SubscribeEvent
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent ev)
    {
        if (ev.isSilkTouching())
            return;

        List<ItemStack> drops = ev.getDrops();
        if (drops instanceof ImmutableList)
        {
            Survivalist.logger.warn("WARNING: Some mod is returning an ImmutableList from HarvestBlocks, replacing drops will NOT be possible.");
            return;
        }

        boolean anyChanged = false;
        List<ItemStack> newDrops = Lists.newArrayList();

        int fortune = ev.getFortuneLevel();

        for (ItemStack drop : drops)
        {
            if (drop.getCount() <= 0)
                continue;

            if (drop.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) && ConfigManager.instance.replaceStoneDrops)
            {
                newDrops.add(new ItemStack(Survivalist.rock, 4));
                anyChanged = true;
            }
            else if (drop.getItem() == Item.getItemFromBlock(Blocks.STONE) && ConfigManager.instance.replaceStoneDrops)
            {
                switch (drop.getMetadata())
                {
                    case 0:
                        newDrops.add(new ItemStack(Survivalist.rock, 4, 0));
                        anyChanged = true;
                        break;
                    case 5:
                        newDrops.add(new ItemStack(Survivalist.rock, 4, 1));
                        anyChanged = true;
                        break;
                    case 3:
                        newDrops.add(new ItemStack(Survivalist.rock, 4, 2));
                        anyChanged = true;
                        break;
                    case 1:
                        newDrops.add(new ItemStack(Survivalist.rock, 4, 3));
                        anyChanged = true;
                        break;
                    default:
                        newDrops.add(drop);
                }
            }
            else if (drop.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) && ConfigManager.instance.replaceIronOreDrops)
            {
                newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 0));
                anyChanged = true;
            }
            else if (drop.getItem() == Item.getItemFromBlock(Blocks.GOLD_ORE) && ConfigManager.instance.replaceGoldOreDrops)
            {
                newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 1));
                anyChanged = true;
            }
            else if (ConfigManager.instance.replaceModOreDrops)
            {
                if (OreDictionaryHelper.hasOreName(drop, "oreCopper"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 2));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "oreTin"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 3));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "oreLead"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 4));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "oreSilver"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 5));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "oreAluminum") || OreDictionaryHelper.hasOreName(drop, "oreAluminium"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountNormal(), fortune), 6));
                    anyChanged = true;
                }
                else
                {
                    newDrops.add(drop);
                }
            }
            else if (ConfigManager.instance.replacePoorOreDrops)
            {
                if (OreDictionaryHelper.hasOreName(drop, "orePoorIron"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 0));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorgold"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 1));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorCopper"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 2));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorTin"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 3));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorLead"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 4));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorSilver"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 5));
                    anyChanged = true;
                }
                else if (OreDictionaryHelper.hasOreName(drop, "orePoorAluminum") || OreDictionaryHelper.hasOreName(drop, "orePoorAluminium"))
                {
                    newDrops.add(new ItemStack(Survivalist.rock_ore, applyFortune(getAmountPoor(), fortune), 6));
                    anyChanged = true;
                }
                else
                {
                    newDrops.add(drop);
                }
            }
            else
            {
                newDrops.add(drop);
            }
        }

        if (anyChanged)
        {
            drops.clear();
            drops.addAll(newDrops);
        }
    }

    private int getAmountPoor()
    {
        return 1 + Math.round(rnd.nextFloat());
    }

    private int getAmountNormal()
    {
        return 2 + Math.round(2 * rnd.nextFloat());
    }

    private int applyFortune(int amount, int fortune)
    {
        int i = rnd.nextInt(fortune + 2) - 1;

        if (i < 0)
        {
            i = 0;
        }

        return amount * (i + 1);
    }
}
