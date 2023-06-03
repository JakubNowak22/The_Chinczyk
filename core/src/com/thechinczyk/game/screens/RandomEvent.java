package com.thechinczyk.game.screens;

import java.util.Random;


public class RandomEvent {
    String cardMessage;
    MiniGamesTypes miniGameType;
    static DayParkMap map;
    int nextRoundMovement, thisRoundMovement;
    int roundsMissed;
    private static final int RANDOM_EVENTS_NUMBER = 11;
    private static final int SPECIAL_FIELDS_COUNTER = 7;
    private static final int[] SPECIAL_FIELDS_NUMBERS = {3, 11, 16, 20, 25, 37, 43};
    public static final int ICE_CREAM_SPECIAL_FIELD_NUMBER = 16;
    public static final int TRAMPOLINE_SPECIAL_FIELD_NUMBER = 43;
    private static final int[] DEVELOPER_FIELDS = {0, 7, 24, 29, 7, 24, 29};
    public static final int[] ICE_CREAM_EVENTS = {7,8};
    public static final int[] TRAMPOLINE_EVENTS = {9,10};



    public RandomEvent(String message, MiniGamesTypes type, int nextRoundMovement, int thisRoundMovement, int roundsMissed) {
        this.cardMessage = message;
        this.miniGameType = type;
        this.nextRoundMovement = nextRoundMovement;
        this.roundsMissed = roundsMissed;
        this.thisRoundMovement = thisRoundMovement;
    }

    public static void setMap(DayParkMap map) {
        RandomEvent.map = map;
    }

    public static boolean checkIsFieldSpecial(int fieldNumber) {
        for (int i = 0; i<SPECIAL_FIELDS_COUNTER; i++)  {
            if (fieldNumber != DEVELOPER_FIELDS[i])
                return true;
        }
     return false;
    }

    public static RandomEvent[] createRandomEventsArray() {
        RandomEvent[] array = new RandomEvent[RANDOM_EVENTS_NUMBER];
        array[0] = new RandomEvent("Mini Game time!\nYou will be playing... SPACE INVADERS", MiniGamesTypes.SPACE_INVADERS, 0, 0,0);
        array[1] = new RandomEvent("Mini Game time!\nYou will be playing... MATH MINI-GAME", MiniGamesTypes.MATH, 0, 0,0);
        array[2] = new RandomEvent("Mini Game time!\nYou will be playing... MEMORY MINI-GAME", MiniGamesTypes.MEMORY, 0, 0,0);
        //array[3] = new RandomEvent("This time you are lucky!\nNext round, you will move two fields more", MiniGamesTypes.NONE);
        array[3] = new RandomEvent("This time you are lucky!\nNext round, you will move four fields more", MiniGamesTypes.NONE, 4, 0,0);
        array[4] = new RandomEvent("This time you are lucky!\nYour pawn moves two fields now", MiniGamesTypes.NONE, 0, 2,0);
        //array[5] = new RandomEvent("Unfortunately, your pawn goes back to the base", MiniGamesTypes.NONE);
        array[5] = new RandomEvent("That's unlucky...\n You will miss next round", MiniGamesTypes.NONE, 0, 0,1);
        array[6] = new RandomEvent("That's unlucky...\n You will miss two next rounds", MiniGamesTypes.NONE, 0, 0,2);
        array[7] = new RandomEvent("You stop for an ice cream.\nIt tastes so delicious, that you miss next round.\n", MiniGamesTypes.NONE, 0, 0,1);
        array[8] = new RandomEvent("You stop for an ice cream.\nIt is so good, that you will move three fields now\n", MiniGamesTypes.NONE, 0,3, 0);
        array[9] = new RandomEvent("There is a trampoline!\nYou jump on it, and it helps you to jump four fields now!", MiniGamesTypes.NONE, 4, 0,0);
        array[10] = new RandomEvent("There is a trampoline!\nYou jump on it, and it helps you to jump two fields now!", MiniGamesTypes.NONE, 2, 0,0);
        return array;
    }

    public static void drawMiniGameOutput() {
        map.drawCardAnim("Mini-game output");
        if (map.miniGameOutput && !map.gameTextures.cardAnimStarted)
            map.miniGameOutput = false;
    }

}
