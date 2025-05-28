package dev.vva;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VillagerTalkCommand {

    private static final ApiService apiService = new ApiService();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("vi_talk")
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    var message = StringArgumentType.getString(context, "message");
                                    var source = context.getSource();

                                    // Log the message to console
                                    Mymod.LOGGER.info("Villager Talk: {}", message);
                                    var env = getEnvironmentInfo(source);
                                    Mymod.LOGGER.info("Villager Talk env: {}", env);

                                    // Send a feedback message to the player
                                    apiService.sendMessage(message, res -> {
                                        Mymod.LOGGER.info("Villager Talk response: {}", res);
                                        source.sendFeedback(() -> Text.literal(String.format("Житель: %s\n", res)), false);
                                    });
                                    return 1;
                                }))
        );
    }

    private static String getEnvironmentInfo(ServerCommandSource source) {
        StringBuilder info = new StringBuilder();

        try {
            // Get the server world
            var world = source.getWorld();

            // Get the player or entity position
            var entity = source.getEntity();
            BlockPos pos;

            if (entity != null) {
                pos = entity.getBlockPos();
            } else {
                pos = new BlockPos(0, 64, 0); // Default position if no entity
            }

            // Get biome information
            var biome = world.getBiome(pos);
            String biomeName = biome.getKey().map(key -> key.getValue().getPath()).orElse("unknown");

            // Get weather information
            boolean isRaining = world.isRaining();
            boolean isThundering = world.isThundering();

            // Get time of day
            long timeOfDay = world.getTimeOfDay() % 24000;
            String dayPhase;

            if (timeOfDay < 1000) {
                dayPhase = "Dawn";
            } else if (timeOfDay < 6000) {
                dayPhase = "Day";
            } else if (timeOfDay < 12000) {
                dayPhase = "Afternoon";
            } else if (timeOfDay < 13000) {
                dayPhase = "Dusk";
            } else if (timeOfDay < 18000) {
                dayPhase = "Night";
            } else {
                dayPhase = "Late Night";
            }

            // Get light level
            int lightLevel = world.getLightLevel(pos);

            // Build the information string
            info.append("Biome: ").append(biomeName).append(", ");
            info.append("Weather: ");
            if (isRaining) {
                info.append(isThundering ? "Thunderstorm" : "Rain");
            } else {
                info.append("Clear");
            }
            info.append(", ");
            info.append("Time: ").append(dayPhase).append(" (").append(timeOfDay).append("), ");
            info.append("Light Level: ").append(lightLevel);

        } catch (Exception e) {
            info.append("Error getting environment info: ").append(e.getMessage());
            Mymod.LOGGER.error("Error getting environment info", e);
        }

        return info.toString();
    }
}
