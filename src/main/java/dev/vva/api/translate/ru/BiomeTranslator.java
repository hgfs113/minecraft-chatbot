package dev.vva.api.translate.ru;

import java.util.Map;
import java.util.HashMap;

public class BiomeTranslator {
    private static final Map<String, String> BIOME_TRANSLATIONS = new HashMap<>();

    static {
        BIOME_TRANSLATIONS.put("plains", "Равнины");
        BIOME_TRANSLATIONS.put("sunflower_plains", "Подсолнечные равнины");
        BIOME_TRANSLATIONS.put("savanna", "Саванна");
        BIOME_TRANSLATIONS.put("savanna_plateau", "Плато саванны");
        BIOME_TRANSLATIONS.put("forest", "Лес");
        BIOME_TRANSLATIONS.put("birch_forest", "Березовый лес");
        BIOME_TRANSLATIONS.put("dark_forest", "Темный лес");
        BIOME_TRANSLATIONS.put("flower_forest", "Цветочный лес");
        BIOME_TRANSLATIONS.put("taiga", "Тайга");
        BIOME_TRANSLATIONS.put("old_growth_pine_taiga", "Старорастущая сосновая тайга");
        BIOME_TRANSLATIONS.put("old_growth_spruce_taiga", "Старорастущая еловая тайга");
        BIOME_TRANSLATIONS.put("snowy_taiga", "Снежная тайга");
        BIOME_TRANSLATIONS.put("mountains", "Горы");
        BIOME_TRANSLATIONS.put("snowy_slopes", "Заснеженные склоны");
        BIOME_TRANSLATIONS.put("frozen_peaks", "Замерзшие пики");
        BIOME_TRANSLATIONS.put("stony_peaks", "Каменистые пики");
        BIOME_TRANSLATIONS.put("meadow", "Горный луг");
        BIOME_TRANSLATIONS.put("grove", "Роща");
        BIOME_TRANSLATIONS.put("ocean", "Океан");
        BIOME_TRANSLATIONS.put("deep_ocean", "Глубокий океан");
        BIOME_TRANSLATIONS.put("warm_ocean", "Теплый океан");
        BIOME_TRANSLATIONS.put("cold_ocean", "Холодный океан");
        BIOME_TRANSLATIONS.put("frozen_ocean", "Замерзший океан");
        BIOME_TRANSLATIONS.put("river", "Река");
        BIOME_TRANSLATIONS.put("beach", "Пляж");
        BIOME_TRANSLATIONS.put("desert", "Пустыня");
        BIOME_TRANSLATIONS.put("badlands", "Бесплодные земли");
        BIOME_TRANSLATIONS.put("eroded_badlands", "Эродированные бесплодные земли");
        BIOME_TRANSLATIONS.put("swamp", "Болото");
        BIOME_TRANSLATIONS.put("mangrove_swamp", "Мангровые заросли");
        BIOME_TRANSLATIONS.put("snowy_plains", "Снежные равнины");
        BIOME_TRANSLATIONS.put("ice_spikes", "Ледяные шипы");
        BIOME_TRANSLATIONS.put("dripstone_caves", "Пещеры с капельниками");
        BIOME_TRANSLATIONS.put("lush_caves", "Буйные пещеры");
        BIOME_TRANSLATIONS.put("deep_dark", "Глубокий мрак");
        BIOME_TRANSLATIONS.put("nether_wastes", "Пустоши Нижнего мира");
        BIOME_TRANSLATIONS.put("the_end", "Край");
    }

    public static String translate(String englishBiomeName) {
        return BIOME_TRANSLATIONS.getOrDefault(englishBiomeName.toLowerCase(), englishBiomeName);
    }
}
