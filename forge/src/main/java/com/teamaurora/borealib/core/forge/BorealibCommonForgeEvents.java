package com.teamaurora.borealib.core.forge;

import com.teamaurora.borealib.api.base.v1.util.MutableBoolean;
import com.teamaurora.borealib.api.base.v1.util.MutableFloat;
import com.teamaurora.borealib.api.base.v1.util.MutableInt;
import com.teamaurora.borealib.api.event.entity.v1.LivingEntityEvents;
import com.teamaurora.borealib.api.event.entity.v1.ProjectileImpactEvent;
import com.teamaurora.borealib.api.event.entity.v1.TradeEvents;
import com.teamaurora.borealib.api.event.entity.v1.player.PlayerEvents;
import com.teamaurora.borealib.api.event.entity.v1.player.PlayerInteractionEvents;
import com.teamaurora.borealib.api.event.lifecycle.v1.LevelLifecycleEvents;
import com.teamaurora.borealib.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.teamaurora.borealib.api.event.registry.v1.CommandRegistryEvent;
import com.teamaurora.borealib.api.event.world.v1.ExplosionEvents;
import com.teamaurora.borealib.api.event.world.v1.WorldEvents;
import com.teamaurora.borealib.core.Borealib;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Borealib.MOD_ID)
public class BorealibCommonForgeEvents {

    @SubscribeEvent
    public static void onEvent(ServerAboutToStartEvent event) {
        if (!ServerLifecycleEvents.PRE_STARTING.invoker().forServer(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartingEvent event) {
        if (!ServerLifecycleEvents.STARTING.invoker().forServer(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartedEvent event) {
        ServerLifecycleEvents.STARTED.invoker().forServer(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppingEvent event) {
        ServerLifecycleEvents.STOPPING.invoker().forServer(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppedEvent event) {
        ServerLifecycleEvents.STOPPED.invoker().forServer(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.village.VillagerTradesEvent event) {
        Int2ObjectMap<TradeEvents.TradeList> newTrades = new Int2ObjectOpenHashMap<>();
        int minTier = event.getTrades().keySet().intStream().min().orElse(1);
        int maxTier = event.getTrades().keySet().intStream().max().orElse(5);
        TradeEvents.VILLAGER.invoker().modifyTrades(new TradeEvents.ModifyVillager.Context() {
            @Override
            public VillagerProfession getProfession() {
                return event.getType();
            }

            @Override
            public TradeEvents.TradeList getTrades(int tier) {
                Validate.inclusiveBetween(minTier, maxTier, tier, "Tier must be between " + minTier + " and " + maxTier);
                return newTrades.computeIfAbsent(tier, key -> new TradeEvents.TradeList());
            }

            @Override
            public int getMinTier() {
                return minTier;
            }

            @Override
            public int getMaxTier() {
                return maxTier;
            }
        });

        newTrades.forEach((tier, registry) -> event.getTrades().get(tier.intValue()).addAll(registry));
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.village.WandererTradesEvent event) {
        TradeEvents.TradeList generic = new TradeEvents.TradeList();
        TradeEvents.TradeList rare = new TradeEvents.TradeList();

        TradeEvents.WANDERER.invoker().modifyTrades(new TradeEvents.ModifyWanderer.Context() {
            @Override
            public TradeEvents.TradeList getGeneric() {
                return generic;
            }

            @Override
            public TradeEvents.TradeList getRare() {
                return rare;
            }
        });

        event.getGenericTrades().addAll(generic);
        event.getRareTrades().addAll(rare);
    }

    @SubscribeEvent
    public static void onEvent(LevelEvent.Load event) {
        LevelLifecycleEvents.LOAD.invoker().load(event.getLevel());
    }

    @SubscribeEvent
    public static void onEvent(LevelEvent.Unload event) {
        LevelLifecycleEvents.UNLOAD.invoker().unload(event.getLevel());
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.level.SaplingGrowTreeEvent event) {
        InteractionResult result = WorldEvents.TREE_GROWING.invoker().interaction(event.getLevel(), event.getRandomSource(), event.getPos());
        if (result != InteractionResult.PASS)
            event.setResult(convertResult(result));
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.BonemealEvent event) {
        InteractionResult result = WorldEvents.BONEMEAL.invoker().bonemeal(event.getLevel(), event.getPos(), event.getBlock(), event.getStack());
        if (result == InteractionResult.FAIL)
            event.setCanceled(true);
        else if (result == InteractionResult.SUCCESS)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public static void onEvent(AdvancementEvent event) {
        PlayerEvents.ADVANCEMENT.invoker().playerAdvancement(event.getEntity(), event.getAdvancement());
    }

    @SubscribeEvent
    public static void onEvent(PlayerXpEvent.PickupXp event) {
        if (!PlayerEvents.EXP_PICKUP.invoker().expPickup(event.getEntity(), event.getOrb()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(PlayerXpEvent.XpChange event) {
        if (!PlayerEvents.EXP_CHANGE.invoker().expChange(event.getEntity(), MutableInt.linkToForge(event, PlayerXpEvent.XpChange::getAmount, PlayerXpEvent.XpChange::setAmount)))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(PlayerXpEvent.LevelChange event) {
        if (!PlayerEvents.LEVEL_CHANGE.invoker().levelChange(event.getEntity(), MutableInt.linkToForge(event, PlayerXpEvent.LevelChange::getLevels, PlayerXpEvent.LevelChange::setLevels)))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerSleepInBedEvent event) {
        Player.BedSleepingProblem result = PlayerEvents.START_SLEEPING.invoker().startSleeping(event.getEntity(), event.getPos());
        if (result != null)
            event.setResult(result);
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerWakeUpEvent event) {
        PlayerEvents.STOP_SLEEPING.invoker().stopSleeping(event.getEntity(), event.wakeImmediately(), event.updateLevel());
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEvents.RESPAWN.invoker().respawn((ServerPlayer) event.getEntity(), event.isEndConquered());
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemCraftedEvent event) {
        PlayerEvents.ITEM_CRAFTED.invoker().craft(event.getEntity(), event.getCrafting(), event.getInventory());
    }

    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemSmeltedEvent event) {
        PlayerEvents.ITEM_SMELTED.invoker().smelt(event.getEntity(), event.getSmelting());
    }

    @SubscribeEvent
    public static void onEvent(ShieldBlockEvent event) {
        if (!LivingEntityEvents.SHIELD_BLOCK.invoker().onShieldBlock(event.getEntity(), event.getDamageSource(), event.getOriginalBlockedDamage(),
                MutableFloat.linkToForge(event, ShieldBlockEvent::getBlockedDamage, ShieldBlockEvent::setBlockedDamage), MutableBoolean.linkToForge(event, ShieldBlockEvent::shieldTakesDamage, ShieldBlockEvent::setShieldTakesDamage)))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(LivingDamageEvent event) {
        if (!LivingEntityEvents.DAMAGE.invoker().livingDamage(event.getEntity(), event.getSource(), MutableFloat.linkToForge(event, LivingDamageEvent::getAmount, LivingDamageEvent::setAmount)))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(LivingDeathEvent event) {
        if (!LivingEntityEvents.DEATH.invoker().death(event.getEntity(), event.getSource()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(LivingHealEvent event) {
        if (!LivingEntityEvents.HEAL.invoker().heal(event.getEntity(), MutableFloat.linkToForge(event, LivingHealEvent::getAmount, LivingHealEvent::setAmount)))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.ProjectileImpactEvent event) {
        if (!ProjectileImpactEvent.EVENT.invoker().onProjectileImpact(event.getProjectile(), event.getRayTraceResult()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        InteractionResultHolder<ItemStack> result = PlayerInteractionEvents.RIGHT_CLICK_ITEM.invoker().interact(event.getEntity(), event.getLevel(), event.getHand());
        if (result.getResult() != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result.getResult());
        }
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = PlayerInteractionEvents.RIGHT_CLICK_BLOCK.invoker().interact(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock event) {
        InteractionResult result = PlayerInteractionEvents.LEFT_CLICK_BLOCK.invoker().interact(event.getEntity(), event.getLevel(), event.getHand(), event.getPos(), event.getFace());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = PlayerInteractionEvents.RIGHT_CLICK_ENTITY.invoker().interact(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public static void onEvent(RegisterCommandsEvent event) {
        CommandRegistryEvent.EVENT.invoker().registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.level.ExplosionEvent.Start event) {
        if (!ExplosionEvents.START.invoker().onStart(event.getLevel(), event.getExplosion()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.level.ExplosionEvent.Detonate event) {
        ExplosionEvents.DETONATE.invoker().detonate(event.getLevel(), event.getExplosion(), event.getAffectedEntities());
    }

    public static Event.Result convertResult(InteractionResult result) {
        switch (result) {
            case FAIL:
                return Event.Result.DENY;
            case SUCCESS:
                return Event.Result.ALLOW;
            default:
                return Event.Result.DEFAULT;
        }
    }
}
