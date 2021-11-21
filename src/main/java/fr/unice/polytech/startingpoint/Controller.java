package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    public final static int NUMBER_OF_PLAYER = 4;
    private Game game;
    private PrintCitadels printC;
    private PhaseManager phaseManager;
    private ArrayList<Player> listOfPlayer;
    private DeckCharacter deckCharacter;
    private DeckDistrict deckDistrict;
    private final int Bonus1st = 4;
    private final int BonusEnd = 2;
    private int roundNumber=1;


    public Controller() {
        listOfPlayer = new ArrayList<>();
        deckCharacter = new DeckCharacter();
        deckDistrict = new DeckDistrict();

        game = new Game(listOfPlayer, deckCharacter, deckDistrict);
        printC = new PrintCitadels();
        phaseManager = new PhaseManager();
    }

    public void runGame() {
        boolean res = false;
        while (!res) {
            printC.printNumberRound(roundNumber);
            printC.printBoard(game);
            res = runRound();
            printC.printLayer();
        }
        end();
    }

    public boolean runRound() {
        if (roundNumber!=1) game.updateListOfPlayer();
        startRoundPart1();
        return(startRoundPart2(game.getListOfPlayer()));
    }

    public void end() {
        game.getWinner();
        printC.printRanking(listOfPlayer);
    }

    public void initGame() {
        listOfPlayer = new ArrayList<>();
        deckCharacter = new DeckCharacter();
        deckDistrict = new DeckDistrict();
        for (int i = 1; i <= NUMBER_OF_PLAYER; i++)
            listOfPlayer.add(new Player("robot" + i));
        game = new Game(listOfPlayer, deckCharacter, deckDistrict); // créer un jeu avec tout les éléments nécessaires
    } 

    public void startRoundPart1() {
        deckCharacter.initialise();
        listOfPlayer.forEach(player ->
        {
            player.chooseCharacterCard(deckCharacter.chooseCharacter());
            printC.chooseRole(player, player.getCharacter());
        });
        printC.dropALine();
    } 

    public boolean startRoundPart2(ArrayList<Player> listOfPlayer) {
        boolean isLastRound = false;

        listOfPlayer.forEach(player->
        {
            player.getCharacter().spellOfBeginningOfRound(player,game);
        });

        ArrayList<City> listOfCity = listOfPlayer.stream().
        										  map(p -> p.getCity()).
        										  collect(Collectors.toCollection(ArrayList::new));
        String currentPhase = phaseManager.analyseGame(listOfCity);
        for (Player player : listOfPlayer) {

            // Instantiates a bot that will make decisions for the player.
            Bot bot = new Bot(player);
            bot.botStartRoundPart2(deckDistrict, currentPhase);

            boolean res = bot.play();
            if (res) {
                printC.printPlayerToCompleteCity(player);
                if (!isLastRound) {
                	isLastRound = true;
                    printC.printFirstPlayerToComplete(player);
                    player.updateScore(Bonus1st);
                    currentPhase = PhaseManager.LAST_TURN_PHASE;
                } else {
                    player.updateScore(BonusEnd);
                }
            }
        }
        printC.dropALine();
        roundNumber++;
        return isLastRound;
    }
    public Game getGame(){
    	return game;
    } 

}