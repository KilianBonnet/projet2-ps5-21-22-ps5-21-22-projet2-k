package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Controller {
    private int numberOfPlayer = 4;
    private Game game;
    private PrintCitadels printC;
    private ArrayList<Player> listOfPlayer;
    private DeckCharacter deckCharacter;
    private DeckDistrict deckDistrict;
    private final int Bonus1st = 4;
    private final int BonusEnd = 2;


    public Controller() {
        listOfPlayer = new ArrayList<>();

        deckCharacter = new DeckCharacter();
        deckDistrict = new DeckDistrict();

        game = new Game(listOfPlayer, deckCharacter, deckDistrict);
        printC = new PrintCitadels();
    }

    public void runGame() {
        boolean res = false;
        while (!res) {
            res = runRound();
            printC.printLayer();
        }
        end();
    }

    public boolean runRound() {
        startRoundPart1();
        Optional<Player> firstWinner = startRoundPart2();
        return (firstWinner.isPresent());
    }

    public void end() {
        game.getWinner();
        printC.printRanking(listOfPlayer);
    }

    public void initGame() {
        listOfPlayer = new ArrayList<>();
        deckCharacter = new DeckCharacter();
        deckDistrict = new DeckDistrict();
        for (int i = 1; i <= numberOfPlayer; i++)
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

    public Optional<Player> startRoundPart2() {
        boolean lastRound = false;
        Optional<Player> firstPlayerToComplete = Optional.empty();

        for (Player player : listOfPlayer) {
            player.chooseDistictCard(deckDistrict.chooseDistrict());
            //car pour l'instant un seul district
            printC.chooseDistrict(player, player.getDistrictCards().get(0));
            boolean res = player.play();
            if (res) {
                printC.printPlayerToCompleteCity(player);
                if (!lastRound) {
                    lastRound = true;
                    firstPlayerToComplete = Optional.of(player);
                    printC.printFirstPlayerToComplete(player);
                    player.updateScore(Bonus1st);
                } else {
                    player.updateScore(BonusEnd);
                }
            }
        }
        printC.dropALine();
        return firstPlayerToComplete;
    }
}