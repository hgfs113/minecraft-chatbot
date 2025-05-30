package dev.vva.api.translate.ru;

public class LightLevelTranslator {

    public static String getLightDescription(int lightLevel) {
        if (lightLevel < 0 || lightLevel > 15) {
            throw new IllegalArgumentException("Light level must be between 0 and 15");
        }

        return switch (lightLevel) {
            case 0 -> "Полная тьма";
            case 1, 2 -> "Очень темно";
            case 3, 4, 5 -> "Темно";
            case 6, 7 -> "Сумрак";
            case 8, 9, 10 -> "Приглушенный свет";
            case 11, 12 -> "Умеренный свет";
            case 13, 14 -> "Яркий свет";
            case 15 -> "Максимальный свет";
            default -> "неизвестно";
        };
    }

    public static boolean canMobsSpawn(int lightLevel) {
        return lightLevel <= 7;
    }

    public static boolean isSafeForPlayer(int lightLevel) {
        return lightLevel >= 8;
    }
}
