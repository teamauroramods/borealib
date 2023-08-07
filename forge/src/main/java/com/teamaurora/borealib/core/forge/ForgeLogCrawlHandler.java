package com.teamaurora.borealib.core.forge;

import com.google.common.base.Predicates;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.forge.BorealibHollowLogBlock;
import com.teamaurora.borealib.api.config.v1.ConfigRegistry;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Borealib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeLogCrawlHandler {

    // Hacky workaround to access Quark's config options. Not really any better way to do this
    public static boolean autoCrawlEnabled() {
        Optional<ModConfig> cfg = ConfigRegistry.get(Mods.QUARK, ModConfig.Type.COMMON);
        return cfg.isPresent() ? cfg.get().getConfigData().get("building.hollow_logs.Enable Auto Crawl") : false;
    }

    private static final String TAG_TRYING_TO_CRAWL = "quark:trying_crawl";
    private static final ResourceLocation TRIGGER_NAME = new ResourceLocation("quark:hollow_log_crawl");

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if(autoCrawlEnabled() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            BlockPos playerPos = player.blockPosition();
            boolean isTrying = player.isVisuallyCrawling() ||
                    (player.isCrouching() && !player.isColliding(playerPos, player.level().getBlockState(playerPos)));
            boolean wasTrying = player.getPersistentData().getBoolean(TAG_TRYING_TO_CRAWL);

            if (!player.isVisuallyCrawling()) {
                if (isTrying && !wasTrying) {
                    Direction dir = player.getDirection();
                    Direction opp = dir.getOpposite();
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos pos = playerPos.relative(dir);

                        if (!tryClimb(player, opp, playerPos)) // Crawl out
                            if (!tryClimb(player, opp, playerPos.above())) // Crawl out
                                if (!tryClimb(player, dir, pos)) // Crawl into
                                    tryClimb(player, dir, pos.above()); // Crawl into
                    }
                }
            }

            if(isTrying != wasTrying)
                player.getPersistentData().putBoolean(TAG_TRYING_TO_CRAWL, isTrying);
        }
    }

    private boolean tryClimb(Player player, Direction dir, BlockPos pos) {
        BlockState state = player.level().getBlockState(pos);
        Block block = state.getBlock();

        if(block instanceof BorealibHollowLogBlock crawlSpace) {
            if(crawlSpace.canCrawl(player.level(), state, pos, dir)) {
                player.setPose(Pose.SWIMMING);
                player.setSwimming(true);

                double x = pos.getX() + 0.5 - (dir.getStepX() * 0.3);
                double y = pos.getY() + 0.13;
                double z = pos.getZ() + 0.5 - (dir.getStepZ() * 0.3);

                player.setPos(x, y, z);

                if(player instanceof ServerPlayer sp)
                    ((SimpleCriterionTrigger<?>) CriteriaTriggers.getCriterion(TRIGGER_NAME)).trigger(sp, __ -> true);

                return true;
            }
        }

        return false;
    }
}
