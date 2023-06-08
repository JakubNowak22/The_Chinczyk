package com.thechinczyk.game.screens;

public class RandomEvent {
    String cardMessage;
    MiniGamesTypes miniGameType;
    static DayParkMap map;
    int nextRoundMovement;
    int roundsMissed;
    MiniGameOutput result;
    boolean pawnToBase;
    private static final int RANDOM_EVENTS_NUMBER = 15;
    private static final int SPECIAL_FIELDS_COUNTER = 5;
    private static final int[] SPECIAL_FIELDS_NUMBERS = {3, 11, 20, 25, 37};
    public static final int ICE_CREAM_SPECIAL_FIELD_NUMBER = 16;
    public static final int TRAMPOLINE_SPECIAL_FIELD_NUMBER = 43;
    public static final int ICE_CREAM_EVENTS = 5;
    public static final int TRAMPOLINE_EVENTS = 7;
    public static final int BIG_WIN_EVENTS = 9;
    public static final int SMALL_WIN_EVENTS = 11;
    public static final int LOSE_EVENTS = 13;



    public RandomEvent(String message, MiniGamesTypes type, int nextRoundMovement, int roundsMissed, MiniGameOutput result, boolean pawnToBase) {
        this.cardMessage = message;
        this.miniGameType = type;
        this.nextRoundMovement = nextRoundMovement;
        this.roundsMissed = roundsMissed;
        this.result = result;
        this.pawnToBase = pawnToBase;
    }

    public static void setMap(DayParkMap map) {
        RandomEvent.map = map;
    }

    public static boolean checkIsFieldSpecial(int fieldNumber) {
        for (int i = 0; i<SPECIAL_FIELDS_COUNTER; i++)  {
            if (fieldNumber == SPECIAL_FIELDS_NUMBERS[i])
                return true;
        }
     return false;
    }

    public static RandomEvent[] createRandomEventsArray() {
        RandomEvent[] array = new RandomEvent[RANDOM_EVENTS_NUMBER];
        array[0] = new RandomEvent("Mini Game time!\nYou will be playing... SPACE INVADERS", MiniGamesTypes.SPACE_INVADERS, 0,0, MiniGameOutput.NONE, false);
        array[1] = new RandomEvent("Mini Game time!\nYou will be playing... MATH MINI-GAME", MiniGamesTypes.MATH, 0,0, MiniGameOutput.NONE, false);
        array[2] = new RandomEvent("Mini Game time!\nYou will be playing... MEMORY MINI-GAME", MiniGamesTypes.MEMORY, 0,0, MiniGameOutput.NONE, false);
        array[3] = new RandomEvent("This time you are lucky!\nNext round, you will move four fields more", MiniGamesTypes.NONE, 4,0, MiniGameOutput.NONE, false);
        array[4] = new RandomEvent("That's unlucky...\n You will miss next round", MiniGamesTypes.NONE, 0,1, MiniGameOutput.NONE, false);
        array[5] = new RandomEvent("You stop for an ice cream.\nIt tastes so delicious, that you miss next round.\n", MiniGamesTypes.NONE, 0,1, MiniGameOutput.NONE, false);
        array[6] = new RandomEvent("You stop for an ice cream.\nIt is so good, that you will move three extra fields next round\n", MiniGamesTypes.NONE, 3,0, MiniGameOutput.NONE, false);
        array[7] = new RandomEvent("There is a trampoline!\nYou jump on it, and it gives you four extra fields!", MiniGamesTypes.NONE, 4, 0, MiniGameOutput.NONE, false);
        array[8] = new RandomEvent("There is a trampoline!\nYou jump on it, and it gives you two extra fields!", MiniGamesTypes.NONE, 2, 0, MiniGameOutput.NONE, false);
        array[9] = new RandomEvent("Mini-game output!\nGreat result means, that you will move six more fields next round!", MiniGamesTypes.NONE, 6, 0, MiniGameOutput.BIG_WIN, false);
        array[10] = new RandomEvent("Mini-game output!\nGreat result means, that you will move five more fields next round!", MiniGamesTypes.NONE, 5, 0, MiniGameOutput.BIG_WIN, false);
        array[11] = new RandomEvent("Mini-game output!\nGood result means, that next round, \nyou will move two fields more.", MiniGamesTypes.NONE, 2, 0, MiniGameOutput.SMALL_WIN, false);
        array[12] = new RandomEvent("Mini-game output!\nGood result means, that next round, \nyou will move three fields more.", MiniGamesTypes.NONE, 3, 0, MiniGameOutput.SMALL_WIN, false);
        array[13] = new RandomEvent("Mini-game output!\nIt could be better, which means \nthat you will miss next two rounds.", MiniGamesTypes.NONE, 0, 2, MiniGameOutput.LOSE, false);
        array[14] = new RandomEvent("Mini-game output!\nIt could be better, which means \nthat your pawn will go back to the base.", MiniGamesTypes.NONE, 0, 0, MiniGameOutput.LOSE, true);
        return array;
    }

    public static void drawMiniGameOutput(Player player, Pawn pawn) {
        map.miniGameResultToRandomEvent(player, pawn);

        if (map.miniGameOutput && !map.gameTextures.cardAnimStarted) {
            map.miniGameOutput = false;
            map.miniGameResult = MiniGameOutput.NONE;
        }
    }

}
