package dev.vva.handlers;

import dev.vva.Mymod;
import dev.vva.api.translate.ru.BiomeTranslator;
import dev.vva.api.translate.ru.LightLevelTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class HandlerUtils {

    protected static Map<String, String> buildEnvironmentInfo(ServerCommandSource source) {
        return buildEnvironmentInfo(source.getEntity(), source.getWorld());
    }
    protected static Map<String, String> buildEnvironmentInfo(Entity entity, World world) {
        Map<String, String> envInfo = new HashMap<>();
        try {
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
