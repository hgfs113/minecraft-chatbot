package dev.vva.handlers;

import dev.vva.Mymod;
import dev.vva.api.ApiService;
import dev.vva.api.TalkRequest;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Map;

import static dev.vva.handlers.HandlerUtils.buildEnvironmentInfo;
import static dev.vva.handlers.HandlerUtils.getVillagerMood;
import static net.minecraft.entity.ai.brain.Activity.IDLE;
import static net.minecraft.entity.ai.brain.Activity.MEET;
import static net.minecraft.entity.ai.brain.Activity.PANIC;
import static net.minecraft.entity.ai.brain.Activity.RAID;
import static net.minecraft.entity.ai.brain.Activity.REST;
import static net.minecraft.entity.ai.brain.Activity.WORK;

public class VillagerInteractionHandler {
    private static final ApiService apiService = new ApiService();

    public ActionResult handleVillagerInteraction(World world, PlayerEntity player, VillagerEntity villager, Hand hand) {
        // Your custom logic here
        if (shouldShowCustomInterface(player, villager)) {
            return openCustomScreen(world, player, villager);
        }
        return ActionResult.PASS; // Allow normal trading
    }

    private boolean shouldShowCustomInterface(PlayerEntity player, VillagerEntity villager) {
        return true;
    }

    private ActionResult openCustomScreen(World world, PlayerEntity player, VillagerEntity villager) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            // Send custom packet or use built-in screen
            Mymod.LOGGER.info("Interaction has happened");
            var env = buildEnvironmentInfo(player, world);
            var request = new TalkRequest(
                    String.valueOf(villager.getId()),
                    "как дела?",
                    env,
                    getVillagerMood(player, villager)
            );

            apiService.sendMessage(request, res -> {
                Mymod.LOGGER.info("Villager Talk response: {}", res);

            });
        }
        return ActionResult.SUCCESS; // Prevents default trading GUI
    }
}
