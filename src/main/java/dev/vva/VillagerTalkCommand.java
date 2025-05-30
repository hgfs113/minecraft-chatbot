package dev.vva;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vva.api.ApiService;
import dev.vva.api.translate.ru.BiomeTranslator;
import dev.vva.api.TalkRequest;
import dev.vva.api.translate.ru.LightLevelTranslator;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

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
                                    var env = buildEnvironmentInfo(source);
                                    Mymod.LOGGER.info("Villager Talk env: {}", env);

                                    // Send a feedback message to the player
                                    var request = new TalkRequest(message, env);
                                    source.sendFeedback(() -> Text.literal(String.format("Игрок: %s\n", message)), false);
                                    apiService.sendMessage(request, res -> {
                                        Mymod.LOGGER.info("Villager Talk response: {}", res);
                                        source.sendFeedback(() -> Text.literal(String.format("Житель: %s\n", res)), false);
                                    });
                                    return 1;
                                }))
        );
    }

    private static Map<String, String> buildEnvironmentInfo(ServerCommandSource source) {
        Map<String, String> envInfo = new HashMap<>();
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
            var biomeName = biome.getKey().map(key -> key.getValue().getPath()).map(BiomeTranslator::translate).orElse("неизвестно");

            // Get weather information
            boolean isRaining = world.isRaining();
            boolean isThundering = world.isThundering();

            // Get time of day
            long timeOfDay = world.getTimeOfDay() % 24000;
            String dayPhase;

            if (timeOfDay < 1000) {
                dayPhase = "рассвет";
            } else if (timeOfDay < 6000) {
                dayPhase = "день";
            } else if (timeOfDay < 12000) {
                dayPhase = "вечер";
            } else if (timeOfDay < 13000) {
                dayPhase = "сумерки";
            } else if (timeOfDay < 18000) {
                dayPhase = "ночь";
            } else {
                dayPhase = "поздняя ночь";
            }

            // Get light level
            int lightLevel = world.getLightLevel(pos);

            // Build the information string
            envInfo.put("биом", biomeName);
            envInfo.put("погода", isRaining ? isThundering ? "гроза": "дождь": "ясно");
            envInfo.put("освещенность", LightLevelTranslator.getLightDescription(lightLevel));
            envInfo.put("фаза дня", dayPhase);
        } catch (Exception e) {
            Mymod.LOGGER.error("Error getting environment info", e);
        }

        return envInfo;
    }
}
