package fr.unice.polytech.citadelle;

import fr.unice.polytech.citadelle.game.*;
import fr.unice.polytech.citadelle.game.purple_districts.Graveyard;
import fr.unice.polytech.citadelle.game.purple_districts.Observatory;
import fr.unice.polytech.citadelle.game.purple_districts.Smithy;
import fr.unice.polytech.citadelle.game_character.*;
import fr.unice.polytech.citadelle.game_character.Character;
import fr.unice.polytech.citadelle.game_engine.Initializer;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Behaviour;
import fr.unice.polytech.citadelle.game_interactor.game_strategy.Strategy;

import fr.unice.polytech.citadelle.output.PrintCitadels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class StrategyTest {

	private final int bonusForFiveDistrict = 3;

	Board board;
	District green01;
	District green03;
	District green06;
	District yellow02;
	District blue03;
	District purple04;
	District red05;
	private LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters;
	Behaviour botArchitecte;
	Behaviour botBishop;
	Behaviour botMagician;
	Behaviour botAssassin;
	Behaviour botKing;
	Behaviour botThief;
	Behaviour botMerchant;
	Behaviour botWarlord;
	Architect architect;
	Bishop bishop;
	Magician magician;
	Assassin assassin;
	King king;
	Thief thief;
	Merchant merchant;
	Warlord warlord;

	DeckDistrict deckDistrict;
	private Strategy strategy;

	@BeforeEach
	public void init() {
		PrintCitadels.activateLevelWarning();

		hashOfCharacters = new LinkedHashMap<>();
		deckDistrict = new DeckDistrict();
		board = new Board(new ArrayList<Player>(), new ArrayList<Character>(), deckDistrict, new DeckCharacter(4));
		board.getDeckDistrict().initialise();

		// creation of Behaviour
		botArchitecte = new Behaviour(new Player("architectePlayer"), board);
		botBishop = new Behaviour(new Player("bishopPlayer"), board);
		botMagician = spy(new Behaviour(new Player("magicianPlayer"), board));
		botAssassin = new Behaviour(new Player("assassinPlayer"), board);
		botKing = new Behaviour(new Player("kingPlayer"), board);
		botThief = spy(new Behaviour(new Player("thiefPlayer"), board));
		botMerchant = new Behaviour(new Player("merchantPlayer"), board);
		botWarlord = new Behaviour(new Player("warlordPlayer"), board);

		// creation of the characters in game
		architect = new Architect();
		bishop = new Bishop();
		magician = new Magician();
		assassin = new Assassin();
		king = new King();
		thief = new Thief();
		merchant = new Merchant();
		warlord = new Warlord();

		// we set the character of our bot
		botKing.getPlayer().setRole(king);
		botMerchant.getPlayer().setRole(merchant);
		botMagician.getPlayer().setRole(magician);
		botArchitecte.getPlayer().setRole(architect);
		botWarlord.getPlayer().setRole(warlord);
		botThief.getPlayer().setRole(thief);
		botAssassin.getPlayer().setRole(assassin);
		botBishop.getPlayer().setRole(bishop);

		// Adding the character in the Board
		board.getListOfCharacter().add(assassin);
		board.getListOfCharacter().add(thief);
		board.getListOfCharacter().add(magician);
		board.getListOfCharacter().add(king);
		board.getListOfCharacter().add(bishop);
		board.getListOfCharacter().add(merchant);
		board.getListOfCharacter().add(architect);
		board.getListOfCharacter().add(warlord);

		// Adding the players in the Board
		board.getListOfPlayer().add(botAssassin.getPlayer());
		board.getListOfPlayer().add(botThief.getPlayer());
		board.getListOfPlayer().add(botMagician.getPlayer());
		board.getListOfPlayer().add(botKing.getPlayer());
		board.getListOfPlayer().add(botBishop.getPlayer());
		board.getListOfPlayer().add(botMerchant.getPlayer());
		board.getListOfPlayer().add(botArchitecte.getPlayer());
		board.getListOfPlayer().add(botWarlord.getPlayer());
		// creation of the hashOfCharacter
		board.gethashOfViewCharacters().put(botBishop.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botAssassin.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botThief.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botMagician.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botKing.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botMerchant.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botArchitecte.getPlayer(),Optional.empty());
		board.gethashOfViewCharacters().put(botWarlord.getPlayer(),Optional.empty());
		hashOfCharacters.put(bishop, Optional.of(botBishop));
		hashOfCharacters.put(assassin, Optional.of(botAssassin));
		hashOfCharacters.put(thief, Optional.of(botThief));
		hashOfCharacters.put(magician, Optional.of(botMagician));
		hashOfCharacters.put(king, Optional.of(botKing));
		hashOfCharacters.put(merchant, Optional.of(botMerchant));
		hashOfCharacters.put(architect, Optional.of(botArchitecte));
		hashOfCharacters.put(warlord, Optional.of(botWarlord));

		green01 = new District("greenDistrict", 1, "Green", "empty");
		green03 = new District("greenDistrict", 6, "Green", "empty");
		green06 = new District("greenDistrict", 6, "Green", "empty");

		yellow02 = new District("greenDistrict", 2, "Yellow", "empty");
		blue03 = new District("greenDistrict", 3, "Blue", "empty");
		purple04 = new District("greenDistrict", 4, "Purple", "empty");
		red05 = new District("greenDistrict", 5, "Red", "empty");

		strategy = new Strategy(8, board, botMagician.getPlayer());

	}

	@RepeatedTest(1)
	void chooseCharacterForThiefRandom() {

		Strategy strategy = new Strategy(8, null, botThief.getPlayer());
		Character characterChoosen = strategy.chooseCharacterForThiefRandom(hashOfCharacters);
		assertNotEquals("Assassin", characterChoosen);
		assertNotEquals("Thief", characterChoosen);
		assertEquals(true, characterChoosen.getCharacterisAlive());
	}

	@RepeatedTest(1)
	void chooseCharacterForAssassinRandom() {
		Strategy strategy = new Strategy(8, null, botAssassin.getPlayer());
		Character characterChoosen = strategy.chooseCharacterForAssassinRandom(hashOfCharacters);
		assertNotEquals("Assassin", characterChoosen);
	}


	@RepeatedTest(1)
	//@Test
	void playerHasOnlyFiveDifferentDistrictColor() {
		botMagician.buildDistrict(green01);
		botMagician.buildDistrict(yellow02);
		botMagician.buildDistrict(blue03);
		botMagician.buildDistrict(purple04);
		botMagician.buildDistrict(red05);

		assertTrue(strategy.hasFiveDistrictColors(botMagician.getPlayer()));
	}

	@RepeatedTest(1)
	//@Test
	void playerHasOnlyFourDifferentDistrictColor() {
		botMagician.buildDistrict(green01);
		botMagician.buildDistrict(green03);
		botMagician.buildDistrict(green06);
		botMagician.buildDistrict(yellow02);
		botMagician.buildDistrict(blue03);
		botMagician.buildDistrict(red05);

		assertFalse(strategy.hasFiveDistrictColors(botMagician.getPlayer()));
	}

	@RepeatedTest(1)
	//@Test
	void cityScoreCalculate01() {
		botMagician.buildDistrict(green01); // District value is 1
		botMagician.buildDistrict(green06); // District value is 6
		botMagician.buildDistrict(purple04); // District value is 4
		botMagician.buildDistrict(red05); // District value is 5

		assertEquals((1 + 6 + 4 + 5), strategy.playerPredictScore(botMagician.getPlayer()));
	}

	@RepeatedTest(1)
	//@Test
	void cityScoreCalculate02() {
		botMagician.buildDistrict(green01); // District value is 1
		botMagician.buildDistrict(green06); // District value is 6
		botMagician.buildDistrict(yellow02); // District value is 2
		botMagician.buildDistrict(blue03); // District value is 3
		botMagician.buildDistrict(purple04); // District value is 4
		botMagician.buildDistrict(red05); // District value is 5

		assertEquals((1 + 6 + 2 + 3 + 4 + 5 + bonusForFiveDistrict), strategy.playerPredictScore(botMagician.getPlayer()));
	}

	@RepeatedTest(1)
	//@Test
	void testchooseCharacterForAssassinAdvancedEgality() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		// This is the bot that is going to use the strategy
		botAssassin.buildDistrict(yellow02); // District value is 2
		botAssassin.buildDistrict(red05); // District value is 5
		// he has the same score
		botMagician.buildDistrict(green01); // District value is 1
		botMagician.buildDistrict(green06); // District value is 6
		// he has 2 points different
		botBishop.buildDistrict(yellow02); // District value is 2
		botBishop.buildDistrict(blue03); // District value is 3

		assertEquals(botMagician.getPlayer(), strategy.findThePlayerWithClosestScoreAssassin());
		assertEquals(thief,strategy.chooseCharacterForAssassinAdvanced());

	}

	@RepeatedTest(1)
	//@Test
	void testchooseCharacterForAssassinAdvancedWhenBothGotLessPoints() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		// This is the bot that is going to use the strategy
		botAssassin.buildDistrict(green01); // District value is 1
		botAssassin.buildDistrict(green06); // District value is 6

		// he has 2 points different
		botArchitecte.buildDistrict(yellow02); // District value is 2
		botArchitecte.buildDistrict(blue03); // District value is 3

		// he has 2 points different
		botMagician.buildDistrict(yellow02); // District value is 2

		strategy.findThePlayerWithClosestScoreAssassin();
		assertEquals(botArchitecte.getPlayer(), strategy.findThePlayerWithClosestScoreAssassin());
		botArchitecte.getPlayer().getDistrictCards().add(new District("",1,"red","noble"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("",1,"red","noble"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("",1,"red","noble"));
		botArchitecte.getPlayer().setGolds(6);
		assertEquals(architect,strategy.chooseCharacterForAssassinAdvanced());
	}

	@RepeatedTest(1)
	//@Test
	void testchooseCharacterForAssassinAdvancedWhenBothGotMorePoints() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		// This is the bot that is going to use the strategy
		botAssassin.buildDistrict(green01); // District value is 1
		botAssassin.buildDistrict(green06); // District value is 6

		// he has 2 points different
		botBishop.buildDistrict(green06); // District value is 6
		botBishop.buildDistrict(blue03); // District value is 3

		// he has 5 points different
		botMagician.buildDistrict(green06); // District value is 6
		botMagician.buildDistrict(blue03); // District value is 3
		botMagician.buildDistrict(green03); // District value is 3

		strategy.findThePlayerWithClosestScoreAssassin();

		assertEquals(botBishop.getPlayer(), strategy.findThePlayerWithClosestScoreAssassin());
		assertEquals(thief,strategy.chooseCharacterForAssassinAdvanced());
	}

	@RepeatedTest(1)
	//@Test
	void testchooseCharacterForAssassinAdvancedWhenOneGotMoreAndOtherGotLess1() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		// This is the bot that is going to use the strategy
		botAssassin.buildDistrict(green01); // District value is 1
		botAssassin.buildDistrict(green06); // District value is 6

		// he has 1 points different
		botBishop.buildDistrict(green06); // District value is 6

		// he has 5 points different
		botMagician.buildDistrict(green06); // District value is 6
		botMagician.buildDistrict(blue03); // District value is 3
		botMagician.buildDistrict(green03); // District value is 3

		strategy.findThePlayerWithClosestScoreAssassin();
		assertEquals(botBishop.getPlayer(), strategy.findThePlayerWithClosestScoreAssassin());
		assertEquals(thief,strategy.chooseCharacterForAssassinAdvanced());
	}
	
	@RepeatedTest(1)
	//@Test
	void testchooseCharacterForAssassinAdvancedWhenOneGotMoreAndOtherGotLess2() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		// This is the bot that is going to use the strategy
		botAssassin.buildDistrict(green06); // District value is 6
		botAssassin.buildDistrict(blue03); // District value is 3
		botAssassin.buildDistrict(green03); // District value is 3

		// he has 1 points different
		botBishop.buildDistrict(green06); // District value is 6

		// he has 5 points different
		botMagician.buildDistrict(green01); // District value is 1
		botMagician.buildDistrict(green06); // District value is 6
		botMagician.buildDistrict(blue03); // District value is 3
		botMagician.buildDistrict(green03); // District value is 3

		strategy.findThePlayerWithClosestScoreAssassin();
		assertEquals(botMagician.getPlayer(), strategy.findThePlayerWithClosestScoreAssassin());
		assertEquals(thief,strategy.chooseCharacterForAssassinAdvanced());

	}

	@RepeatedTest(1)
	//@Test
	void chooseCharacterForAssassinAdvancedWhenTargetWhenCharacterNotReveal() {
		strategy = spy(new Strategy(8, board, botAssassin.getPlayer()));
		ArrayList<Player> listOfPlayerForHash = new ArrayList<>();
		listOfPlayerForHash.add(botArchitecte.getPlayer());
		listOfPlayerForHash.add(botAssassin.getPlayer());
		board.setListOfPlayer(listOfPlayerForHash);
		Initializer.initTheHashOfViewCharacters(board.gethashOfViewCharacters(), listOfPlayerForHash);	
		
		strategy.chooseCharacterForAssassinAdvanced();
		verify(strategy, times(1)).getAPrediction(any(), any());
		assertEquals(thief,strategy.chooseCharacterForAssassinAdvanced());


	}

	
	@RepeatedTest(1)
	void getCharacterOfPlayerTest() {
		strategy = new Strategy(8, board, botAssassin.getPlayer());
		ArrayList<Player> listOfPlayers = board.getListOfPlayer();
		listOfPlayers.clear();

		
		Player player1 = new Player("player1");
		Character testCharacter1 = new Character("testCharacter1", 0);
		player1.setRole(testCharacter1);
		
		Player player2 = new Player("player2");
		Character testCharacter2 = new Character("testCharacter2", 0);
		player2.setRole(testCharacter2);

		
		Player player3 = new Player("player3");
		Character testCharacter3 = new Character("testCharacter3", 0);
		player3.setRole(testCharacter3);
		
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		listOfPlayers.add(player3);

		Initializer.initTheHashOfViewCharacters(board.gethashOfViewCharacters(), listOfPlayers);
		
		Player botPlayer = botAssassin.getPlayer();
		botPlayer= player1;
		board.revealCharacter(player3, testCharacter3);
		board.revealCharacter(player2, testCharacter2);


		Optional<Character> predictCharacter = strategy.getCharacterOfPlayer(player3);
		assertTrue(predictCharacter.isPresent());
		assertEquals(testCharacter3, predictCharacter.get());
	}
	
	@RepeatedTest(1)
	//@Test
	void testchooseCharacterWithMostGolds() {


		board.getListOfPlayer().add(botThief.getPlayer());
		board.getListOfPlayer().add(botMerchant.getPlayer());
		board.getListOfPlayer().add(botMagician.getPlayer());
		strategy = new Strategy(8, board, botThief.getPlayer());
		botThief.getPlayer().setGolds(3);
		botMerchant.getPlayer().setGolds(4);
		botMagician.getPlayer().setGolds(5);
		assertEquals(botMagician.getPlayer(), strategy.findThePlayerWithMostGolds());
		assertEquals(magician,strategy.chooseCharacterForThiefAdvanced());

	}
	
	@RepeatedTest(1)
	//@Test
	void testchooseCharacterWithMostGolds2() {
		//We now Try when the thief has the most golds
		board.getListOfPlayer().clear();
		board.getListOfPlayer().add(botThief.getPlayer());
		board.getListOfPlayer().add(botMerchant.getPlayer());
		board.getListOfPlayer().add(botMagician.getPlayer());

		strategy = new Strategy(8, board, botThief.getPlayer());
		botThief.getPlayer().setGolds(5);
		botMerchant.getPlayer().setGolds(4);
		botMagician.getPlayer().setGolds(3);
		strategy.findThePlayerWithMostGolds();
		assertEquals(botMerchant.getPlayer(), strategy.findThePlayerWithMostGolds());
		assertEquals(magician,strategy.chooseCharacterForThiefAdvanced());
	}

	@RepeatedTest(1)
	//@Test
	void testchooseCharacterWithMostGolds3() {
		//We now Try when the assassin has the most golds
		board.getListOfPlayer().clear();
		Player player1=new Player("p1");
		player1.setRole(new Thief());
		Player player2=new Player("p2");
		player2.setRole(new Assassin());
		Player player3=new Player("p3");
		player3.setRole(new Merchant());

		board.getListOfPlayerWhoPlayed().add(player2);


		board.getListOfPlayer().add(player1);
		board.getListOfPlayer().add(player2);
		board.getListOfPlayer().add(player3);
		strategy = new Strategy(8, board, player1);
		player1.setGolds(5);
		player2.setGolds(7);
		player3.setGolds(3);
		strategy.findThePlayerWithMostGolds();
		assertEquals(player3, strategy.findThePlayerWithMostGolds());
	}

	@RepeatedTest(1)
	//@Test
	public void chooseAssassinTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Player player2 = new Player("Player2");
		Behaviour aBehaviour = new Behaviour(player1, board);

		ArrayList<Player> listOfPlayers = new ArrayList<>();
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		board.setListOfPlayer(listOfPlayers);

		player2.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
		player2.buildDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player2.buildDistrict(new District("Palace",5,"Yellow","Nobility"));
		player2.buildDistrict(new Smithy("Smithy", 5,"Purple","Prestige"));
		player2.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
		player2.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));

		assertEquals(3, strategy.chooseAssassin(aBehaviour));
		assertEquals(new Assassin(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterArchitectTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Behaviour aBehaviour = new Behaviour(player1, board);

		player1.addDistrict(new District("Castle",4,"Yellow","Nobility"));
		player1.addDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player1.addDistrict(new Smithy("Smithy", 5,"Purple","Prestige"));

		player1.setGolds(6);

		assertEquals(0, strategy.chooseArchitect(aBehaviour));
		assertEquals(new Architect(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterMagicianTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Player player2 = new Player("Player2");
		Behaviour aBehaviour = new Behaviour(player1, board);

		ArrayList<Player> listOfPlayers = new ArrayList<>();
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		board.setListOfPlayer(listOfPlayers);

		player2.addDistrict(new District("Castle",4,"Yellow","Nobility"));
		player2.addDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player2.addDistrict(new District("Palace",5,"Yellow","Nobility"));
		player2.addDistrict(new Smithy("Smithy", 5,"Purple","Prestige"));
		player2.addDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));

		assertEquals(2, strategy.chooseMagician(aBehaviour));
		assertEquals(new Magician(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterThiefTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Player player2 = new Player("Player2");
		Behaviour aBehaviour = new Behaviour(player1, board);

		ArrayList<Player> listOfPlayers = new ArrayList<>();
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		board.setListOfPlayer(listOfPlayers);

		player2.setGolds(12);

		assertEquals(5, strategy.chooseThief(aBehaviour));
		assertEquals(new Thief(), strategy.chooseCharacter(aBehaviour));
	}
	
	@RepeatedTest(1)
	//@Test
	public void chooseCharacterKingTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player = new Player("Player");
		Behaviour aBehaviour = new Behaviour(player, board);

		player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
		player.buildDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player.buildDistrict(new District("Palace",5,"Yellow","Nobility"));

		assertEquals(4, strategy.chooseKingOrMerchant(aBehaviour));
		assertEquals(new King(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterMerchantTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player = new Player("Player");
		Behaviour aBehaviour = new Behaviour(player, board);

		player.buildDistrict(new District("Trading Post", 2, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Harbor", 4, "Green", "Trade and Handicrafts"));

		assertEquals(6, strategy.chooseKingOrMerchant(aBehaviour));
		assertEquals(new Merchant(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterKingVsMerchantTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player = new Player("Player");
		Behaviour aBehaviour = new Behaviour(player, board);

		player.buildDistrict(new District("Trading Post", 2, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Harbor", 4, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
		player.buildDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player.buildDistrict(new District("Palace",5,"Yellow","Nobility"));

		assertEquals(4, strategy.chooseKingOrMerchant(aBehaviour));
		assertEquals(new King(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterBishopTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player = new Player("Player");
		Behaviour aBehaviour = new Behaviour(player, board);

		board.getListOfPlayer().clear();
		board.getListOfPlayer().add(player);

		player.buildDistrict(new District("Trading Post", 2, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new District("Docks", 3, "Green", "Trade and Handicrafts"));
		player.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
		player.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
		player.buildDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player.buildDistrict(new Smithy("Smithy", 5,"Purple","Prestige"));

		assertEquals(1, strategy.chooseBishop(aBehaviour));
		assertEquals(new Bishop(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void chooseCharacterWarlordTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Player player2 = new Player("Player2");
		Behaviour aBehaviour = new Behaviour(player1, board);

		ArrayList<Player> listOfPlayers = new ArrayList<>();
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		board.setListOfPlayer(listOfPlayers);

		player2.buildDistrict(new District("Castle",4,"Yellow","Nobility"));
		player2.buildDistrict(new District("Manor", 3,"Yellow","Nobility"));
		player2.buildDistrict(new District("Palace",5,"Yellow","Nobility"));
		player2.buildDistrict(new Smithy("Smithy", 5,"Purple","Prestige"));
		player2.buildDistrict(new Observatory("Observatory", 5,"Purple","Prestige"));
		player2.buildDistrict(new Graveyard("Graveyard", 5,"Purple","Prestige"));
		player2.buildDistrict(new District("Battlefield",3,"Red","Soldiery"));

		assertEquals(7, strategy.chooseWarlord(aBehaviour));
		assertEquals(new Warlord(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
		//Test the last return of the chooseCharacter method from RoundManager
		//which return the first character (Assassin) by default
	void chooseCharacterDefaultTest() {
		board.getDeckCharacter().getDeckCharacter().add(architect);
		board.getDeckCharacter().getDeckCharacter().add(bishop);
		board.getDeckCharacter().getDeckCharacter().add(magician);
		board.getDeckCharacter().getDeckCharacter().add(assassin);
		board.getDeckCharacter().getDeckCharacter().add(king);
		board.getDeckCharacter().getDeckCharacter().add(thief);
		board.getDeckCharacter().getDeckCharacter().add(merchant);
		board.getDeckCharacter().getDeckCharacter().add(warlord);

		Player player1 = new Player("Player1");
		Player player2 = new Player("Player2");
		Behaviour aBehaviour = new Behaviour(player1, board);

		ArrayList<Player> listOfPlayers = new ArrayList<>();
		listOfPlayers.add(player1);
		listOfPlayers.add(player2);
		board.setListOfPlayer(listOfPlayers);

		assertEquals(-1, strategy.chooseAssassin(aBehaviour));
		assertEquals(-1, strategy.chooseArchitect(aBehaviour));
		assertEquals(-1, strategy.chooseMagician(aBehaviour));
		assertEquals(-1, strategy.chooseThief(aBehaviour));
		assertEquals(-1, strategy.chooseKingOrMerchant(aBehaviour));
		assertEquals(-1, strategy.chooseBishop(aBehaviour));
		assertEquals(-1, strategy.chooseWarlord(aBehaviour));
		assertEquals(new Architect(), strategy.chooseCharacter(aBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void isThereAFamilyTestNobility(){
		Player bob = new Player("bob");
		Behaviour bobBehaviour = new Behaviour(bob, board);
		bob.buildDistrict(new District("Nobility district 01", 3, "notImportant", "Nobility"));
		bob.buildDistrict(new District("Nobility district 02", 1, "notImportant", "Nobility"));
		bob.buildDistrict(new District("Nobility district 03", 6, "notImportant", "Nobility"));
		bob.buildDistrict(new District("Other district 01", 1, "notImportant", "otherFamily"));
		bob.buildDistrict(new District("Other district 02", 2, "notImportant", "otherFamily"));
		bob.buildDistrict(new District("Other district 03", 4, "notImportant", "otherFamily"));

		assertEquals(6, bob.getCity().getSizeOfCity());
		assertEquals(Initializer.KING_INDEX, strategy.isThereAFamily(bobBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void isThereAFamilyTestTradeAndHandicrafts(){
		Player alice = new Player("alice");
		Behaviour aliceBehaviour = new Behaviour(alice, board);
		alice.buildDistrict(new District("Trade and Handicrafts district 01", 3, "notImportant", "Trade and Handicrafts"));
		alice.buildDistrict(new District("Trade and Handicrafts district 02", 1, "notImportant", "Trade and Handicrafts"));
		alice.buildDistrict(new District("Trade and Handicrafts district 03", 6, "notImportant", "Trade and Handicrafts"));
		alice.buildDistrict(new District("Nobility district 01", 1, "notImportant", "Nobility"));
		alice.buildDistrict(new District("Nobility district 02", 2, "notImportant", "Nobility"));
		alice.buildDistrict(new District("Other district 01", 4, "notImportant", "otherFamily"));

		assertEquals(6, alice.getCity().getSizeOfCity());
		assertEquals(Initializer.MERCHANT_INDEX, strategy.isThereAFamily(aliceBehaviour));
	}

	@RepeatedTest(1)
	//@Test
	public void isThereAFamilyTestNothing(){

		Player fred = new Player("fred");
		Behaviour fredBehaviour = new Behaviour(fred, board);
		fred.buildDistrict(new District("Trade and Handicrafts district 01", 3, "notImportant", "Trade and Handicrafts"));
		fred.buildDistrict(new District("Trade and Handicrafts district 02", 1, "notImportant", "Trade and Handicrafts"));
		fred.buildDistrict(new District("Other district 01", 4, "notImportant", "otherFamily"));
		fred.buildDistrict(new District("Nobility district 01", 1, "notImportant", "Nobility"));
		fred.buildDistrict(new District("Nobility district 02", 2, "notImportant", "Nobility"));
		fred.buildDistrict(new District("Other district 02", 2, "notImportant", "otherFamily"));

		assertEquals(6, fred.getCity().getSizeOfCity());
		assertTrue(strategy.isThereAFamily(fredBehaviour) == -1);
	}

	@RepeatedTest(1)
	//@Test
	void testChooseSpellForMagicianWhenTargetOtherPlayer(){
		botMagician.getPlayer().getDistrictCards().add(new District(null,1,null,null));
		botArchitecte.getPlayer().getDistrictCards().add(new District(null,1,null,null));
		botArchitecte.getPlayer().getDistrictCards().add(new District(null,1,null,null));


		strategy=new Strategy(8,board,botMagician.getPlayer());

		assertEquals(botArchitecte.getPlayer(),strategy.findThePlayerWithMostCards());
		assertEquals(0,strategy.chooseMagicianActionAdvanced().size());
		assertEquals(true,strategy.isThereAPlayerWithTwoTimesHisDistricts());

		assertEquals(null,strategy.cardToBeSwapped());
	}
	
	@RepeatedTest(1)
	//@Test
	void testChooseSpellForMagicianWhenTargetDeck(){
		District districtDoble =new District("districtDoble",1,"Purple","Prestige");
		botMagician.buildDistrict(new District("districtDoble",1,"Purple","Prestige"));
		botMagician.getPlayer().getDistrictCards().add(districtDoble);
		botMagician.getPlayer().getDistrictCards().add(new District("districtOther",1,"Purple","Prestige"));

		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));


		strategy=new Strategy(8,board,botMagician.getPlayer());

		assertEquals(botArchitecte.getPlayer(),strategy.findThePlayerWithMostCards());

		assertEquals(districtDoble,strategy.chooseMagicianActionAdvanced().get(0));

		assertEquals(false,strategy.isThereAPlayerWithTwoTimesHisDistricts());

		assertEquals(districtDoble,strategy.cardToBeSwapped().get(0));
	}
	
	@RepeatedTest(1)
	//@Test
	void testChooseSpellForMagicianWhenTargetDeck2(){
		District districtDoble =new District("districtDoble",1,"Purple","Prestige");
		District districtOtherDoble =new District("otherDistrictDoble",1,"Purple","Prestige");
		botMagician.buildDistrict(new District("districtDoble",1,"Purple","Prestige"));
		botMagician.buildDistrict(new District("otherDistrictDoble",1,"Purple","Prestige"));
		botMagician.getPlayer().getDistrictCards().add(districtDoble);
		botMagician.getPlayer().getDistrictCards().add(districtOtherDoble);
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));


		strategy=new Strategy(8,board,botMagician.getPlayer());

		assertEquals(botArchitecte.getPlayer(),strategy.findThePlayerWithMostCards());

		assertEquals(districtDoble,strategy.chooseMagicianActionAdvanced().get(0));
		assertEquals(districtOtherDoble,strategy.chooseMagicianActionAdvanced().get(1));

		assertEquals(false,strategy.isThereAPlayerWithTwoTimesHisDistricts());

		assertEquals(districtDoble,strategy.cardToBeSwapped().get(0));
	}
	
	@RepeatedTest(1)
	//@Test
	void testChooseTargetOfSpellMagician(){
		botMagician.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botArchitecte.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botWarlord.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));
		botWarlord.getPlayer().getDistrictCards().add(new District("randomDistrict",1,"Purple","Prestige"));


		strategy=new Strategy(8,board,botMagician.getPlayer());

		assertEquals(botArchitecte.getPlayer(),strategy.findThePlayerWithMostCards());
		assertEquals(true,strategy.isThereAPlayerWithTwoTimesHisDistricts());

	}
}
