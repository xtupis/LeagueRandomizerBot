package org.xtupis.randomizer.ai;

import java.util.Random;

public class AbsoluteMadness {

    private final RandomPosition randomPosition = new RandomPosition();
    private final RandomChampion randomChampion = new RandomChampion();
    private final RandomBuild randomBuild = new RandomBuild();
    private final RandomRunes randomRunes = new RandomRunes();

    public String getMadness() {
        String position = randomPosition.getRandomPosition();
        String champion = randomChampion.getRandomChampion();
        String build = randomBuild.getRandomBuild();
        String runes = randomRunes.getRandomRunes();

        String[] spells = {"Скачок", "Игнайт", "Хил", "Телепорт", "Изнурение", "Очищение", "Кара"};
        Random random = new Random();

        String randomSpell1 = spells[random.nextInt(spells.length)];
        String randomSpell2;

        do {
            randomSpell2 = spells[random.nextInt(spells.length)];
        } while (randomSpell1.equals(randomSpell2));

        return String.format(
                "Пожалуйста, не играйте в рейтинг!\n" +
                        "\uD83D\uDD25 Абсолютное безумие \uD83D\uDD25\n" +
                        "Позиция: %s\n" +
                        "Чемпион: %s\n" +
                        "Сборка: %s\n" +
                        "Заклинания: %s, %s\n" +
                        "Руны:\n" +
                        "%s",
                position, champion, build, randomSpell1, randomSpell2, runes
        );
    }
}
