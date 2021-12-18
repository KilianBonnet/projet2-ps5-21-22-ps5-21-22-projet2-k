package fr.unice.polytech.citadelle;

import fr.unice.polytech.citadelle.characters_class.*;
import fr.unice.polytech.citadelle.game.*;
import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.purple_districts.Graveyard;
import fr.unice.polytech.citadelle.game.purple_districts.Observatory;
import fr.unice.polytech.citadelle.game_engine.Initializer;
import fr.unice.polytech.citadelle.game_interactor.Behaviour;
import fr.unice.polytech.citadelle.game_interactor.Predict;

import fr.unice.polytech.citadelle.game_interactor.Strategy;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PredictTest {

    Board board = new Board(new ArrayList<>(), Initializer.createListOfAllCharacter(), new DeckDistrict(), new DeckCharacter());
    Predict predict = new Predict(board);
    ArrayList<Character> allCharacters = board.getListOfCharacter();
    Strategy strategy;

    @RepeatedTest(100)
    @Test
    void targetableCharactersForPredictWhoIsPlayerTest() {

        ArrayList<Character> untargetableCharacter = new ArrayList<>();
        untargetableCharacter.add(board.getListOfCharacter().get(Initializer.BISHOP_INDEX));
        untargetableCharacter.add(board.getListOfCharacter().get(Initializer.THIEF_INDEX));

        ArrayList<Character> listToReturn = new ArrayList<>();
        for(Character character : allCharacters) {
            if(!character.getName().equals("Bishop") && !character.getName().equals("Thief"))
                listToReturn.add(character);
        }

        assertEquals(listToReturn, predict.targetableCharactersForPredictWhoIsPlayer(untargetableCharacter));
    }

    @RepeatedTest(100)
    @Test
    void allCharactersTest() {
        assertEquals(allCharacters, predict.allCharacters());
    }

    @RepeatedTest(100)
    @Test
    void canBeArchitectTest() {
        Player player = new Player("Player");
        player.addDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.addDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.addDistrict(new District("Battlefield",3,"Red","Soldiery"));

        player.setGolds(6);

        assertTrue(predict.canBeArchitect(player, new ArrayList<>()));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeArchitectTest() {
        Player player = new Player("Player");
        player.addDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.addDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.addDistrict(new District("Battlefield",3,"Red","Soldiery"));

        player.setGolds(6);

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Architect());

        assertFalse(predict.canBeArchitect(player, untargetable));
    }

    //@RepeatedTest(100)
    @Test
    void canBeBishopTest() {
        Player player = new Player("Player");
        player.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Battlefield",3,"Red","Soldiery"));
        player.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Battlefield",3,"Red","Soldiery"));

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeBishop(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeBishopTest() {
        Player player = new Player("Player");
        player.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Battlefield",3,"Red","Soldiery"));
        player.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Battlefield",3,"Red","Soldiery"));

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Bishop());

        assertFalse(predict.canBeBishop(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void canBeKingTest() {
        Player player = new Player("Player");
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeKing(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeKingTest() {
        Player player = new Player("Player");
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new King());

        assertFalse(predict.canBeKing(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void canBeMerchantTest() {
        Player player = new Player("Player");
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeMerchant(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeMerchantTest() {
        Player player = new Player("Player");
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Merchant());

        assertFalse(predict.canBeMerchant(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void canBeThiefTest() {
        Player player = new Player("Player");
        player.setGolds(3);

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeThief(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeThiefTest() {
        Player player = new Player("Player");
        player.setGolds(3);

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Thief());

        assertFalse(predict.canBeThief(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void canBeMagicianTest() {
        Player player = new Player("Player");
        player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeMagician(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeMagicianTest() {
        Player player = new Player("Player");
        player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Magician());

        assertFalse(predict.canBeMagician(player, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void canBeWarlordTest() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        board.getListOfPlayer().add(player1);
        board.getListOfPlayer().add(player2);

        ArrayList<Character> untargetable = new ArrayList<>();

        assertTrue(predict.canBeWarlord(player1, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void cannotBeWarlordTest() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        board.getListOfPlayer().add(player1);
        board.getListOfPlayer().add(player2);

        ArrayList<Character> untargetable = new ArrayList<>();
        untargetable.add(new Warlord());

        assertFalse(predict.canBeWarlord(player1, untargetable));
    }

    @RepeatedTest(100)
    @Test
    void listGetCharacterTest() {
        assertEquals(new Assassin(), predict.listGetCharacter(Initializer.ASSASSIN_INDEX));
        assertEquals(new Architect(), predict.listGetCharacter(Initializer.ARCHITECT_INDEX));
        assertEquals(new Bishop(), predict.listGetCharacter(Initializer.BISHOP_INDEX));
        assertEquals(new King(), predict.listGetCharacter(Initializer.KING_INDEX));
        assertEquals(new Magician(), predict.listGetCharacter(Initializer.MAGICIAN_INDEX));
        assertEquals(new Merchant(), predict.listGetCharacter(Initializer.MERCHANT_INDEX));
        assertEquals(new Thief(), predict.listGetCharacter(Initializer.THIEF_INDEX));
        assertEquals(new Warlord(), predict.listGetCharacter(Initializer.WARLORD_INDEX));
    }

    @RepeatedTest(100)
    @Test
    void playersForPredictWhoIsPlayerTest() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        board.getListOfPlayer().add(player1);
        board.getListOfPlayer().add(player2);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player2);

        assertEquals(players, predict.playersForPredictWhoIsPlayer(player1));
    }

    /*
    @Test
    void chooseCharacterForAssassinAdvancedWhenTargetHasAlreadyRevealCharacter() {
        strategy = new Strategy(8, board, botAssassin.getPlayer());

        ArrayList<Player> listOfPlayerForHash = new ArrayList<>();

        listOfPlayerForHash.add(botArchitecte.getPlayer());
        listOfPlayerForHash.add(botAssassin.getPlayer());

        board.setListOfPlayer(listOfPlayerForHash);
        Initializer.initTheHashOfViewCharacters(board.gethashOfViewCharacters(), listOfPlayerForHash);
        board.revealCharacter(botArchitecte.getPlayer(), architect);

        Character CharacterOfTheTarget = strategy.chooseCharacterForAssassinAdvanced();
        assertEquals(architect, CharacterOfTheTarget);
    }*/

    /*
    //@RepeatedTest(100)
    @Test
    void predictWhoIsPlayerTest() {
        Player player = new Player("Player");
        Player player2 = new Player("Player2");

        player.addDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
        player.addDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
        player.addDistrict(new District("Battlefield",3,"Red","Soldiery"));

        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        player.setGolds(6);

        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
        player2.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));

        board.getListOfPlayer().add(player);
        board.getListOfPlayer().add(player2);

        //player.setGolds(3); Thief

        //player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts")); Magician
        //player.addDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts")); Magician

        assertEquals(new Architect(), predict.predictWhoIsPlayer(player, new ArrayList<>()));
    }*/



}