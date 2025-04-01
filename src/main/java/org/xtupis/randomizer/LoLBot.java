package org.xtupis.randomizer;

import org.xtupis.randomizer.token.TokenPovider;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.xtupis.randomizer.ai.*;

import java.util.List;

public class LoLBot extends TelegramLongPollingBot {

    private final RandomPosition randomPosition = new RandomPosition();
    private final RandomChampion randomChampion = new RandomChampion();
    private final RandomBuild randomBuild = new RandomBuild();
    private final AbsoluteMadness absoluteMadness = new AbsoluteMadness();
    private final RandomRunes randomRunes = new RandomRunes();

    @Override
    public String getBotUsername() {
        return "LeagueRandomizerBot";
    }

    @Override
    public String getBotToken() {
        String TOKEN = TokenPovider.getToken();
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            switch (messageText) {
                case "/start":
                    sendMainMenu(chatId);
                    break;
                case "Случайная позиция":
                    sendRandomPosition(chatId);
                    break;
                case "Случайный чемпион":
                    sendRandomChampion(chatId);
                    break;
                case "Случайная сборка":
                    sendRandomBuild(chatId);
                    break;
                case "Абсолютное безумие":
                    sendAbsoluteMadness(chatId);
                    break;
                case "Случайные руны":
                    sendRandomRunes(chatId);
                    break;
                default:
                    sendMessage(chatId, "Неизвестная команда");
                    break;
            }
        }
    }

    private void sendMainMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите опцию:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Случайная позиция"));
        row1.add(new KeyboardButton("Случайный чемпион"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Случайная сборка"));
        row2.add(new KeyboardButton("Случайные руны"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Абсолютное безумие"));

        keyboardMarkup.setKeyboard(List.of(row1, row2, row3));

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRandomPosition(String chatId) {
        sendMessage(chatId, randomPosition.getRandomPosition());
    }

    private void sendRandomChampion(String chatId) {
        sendMessage(chatId, randomChampion.getRandomChampion());
    }

    private void sendRandomBuild(String chatId) {
        sendMessage(chatId, randomBuild.getRandomBuild());
    }

    private void sendAbsoluteMadness(String chatId) {
        sendMessage(chatId, absoluteMadness.getMadness());
    }

    private void sendRandomRunes(String chatId) {
        sendMessage(chatId, randomRunes.getRandomRunes());
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoLBot bot = new LoLBot();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
