package dev.vva;

import dev.vva.handlers.VillagerInteractionHandler;
import dev.vva.handlers.VillagerTalkCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mymod implements ModInitializer {
	public static final String MOD_ID = "my-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final VillagerInteractionHandler villagerInteractionHandler = new VillagerInteractionHandler();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				VillagerTalkCommand.register(dispatcher));

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof VillagerEntity villager && !world.isClient) {
				return villagerInteractionHandler.handleVillagerInteraction(world, player, villager, hand);
			}
			return ActionResult.PASS;
		});
	}
}