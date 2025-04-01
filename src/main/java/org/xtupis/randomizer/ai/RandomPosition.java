package org.xtupis.randomizer.ai;

import java.util.Random;

public class RandomPosition {
    public String getRandomPosition() {
        String[] positions = {"Верхняя линия", "Средняя линия", "Центральная линия", "Нижняя Линия", "Поддержка"};
        return positions[new Random().nextInt(positions.length)];
    }
}