package dev.vva;

import dev.vva.api.ApiService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class MymodClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof VillagerEntity villager && world.isClient) {
				if (shouldShowCustomInterface(player, villager)) {
					MinecraftClient client = MinecraftClient.getInstance();
					ApiService apiService = new ApiService();
					VillagerConversationScreen screen = new VillagerConversationScreen(villager, apiService);
					client.setScreen(screen);
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
	}

	private boolean shouldShowCustomInterface(PlayerEntity player, VillagerEntity villager) {
		return true;
	}
}