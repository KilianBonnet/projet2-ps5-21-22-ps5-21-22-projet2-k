package fr.unice.polytech.citadelle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Optional;

import fr.unice.polytech.citadelle.game.*;
import fr.unice.polytech.citadelle.game_character.Assassin;
import fr.unice.polytech.citadelle.game_character.Bishop;
import fr.unice.polytech.citadelle.game_character.Warlord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.citadelle.game_character.Character;
import fr.unice.polytech.citadelle.game_engine.Initializer;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Investor;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Richalphonse;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Rusher;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Strategator;
import fr.unice.polytech.citadelle.output.PrintCitadels;

public class filterOfsituationTest {
	Player playerRichalphonse;
	Player playerRusher;
	Player playerInvestor;
	Player playerStrategator;

	DeckDistrict deckDistrict;
	Board board;
	Richalphonse richalphonse;
	Rusher rusher;
	Investor investor;
	Strategator strategator;


	@BeforeEach
	public void init() {
		PrintCitadels.activateLevelWarning();

		deckDistrict  = new DeckDistrict();
		deckDistrict.initialise();
		
		playerRichalphonse = new Player("Player");
		playerRusher = new Player("Player");
		playerInvestor = new Player("playerInvestor");
		playerStrategator = new Player("playerStrategator");
		ArrayList<Player> listOfPlayer = new ArrayList<>();
		listOfPlayer.add(playerRichalphonse);
		listOfPlayer.add(playerRusher);
		listOfPlayer.add(playerInvestor);
		listOfPlayer.add(playerStrategator);


    	board = new Board(listOfPlayer, Initializer.createListOfAllCharacter(), deckDistrict , new DeckCharacter(4));
    	DeckCharacter deckCharacter = board.getDeckCharacter();
    	Initializer.initDeckCharacter(deckCharacter, board.getListOfCharacter());
    	//----create 4 bots
		richalphonse = spy(new Richalphonse(playerRichalphonse, board));
		rusher = spy(new Rusher(playerRusher, board));
		investor = spy(new Investor(playerInvestor, board));
		strategator = spy(new Strategator(playerStrategator, board));
		//-----------------
	
	}
	
	@Test
	public void filterByListOfCharacterNotPickableTest() {
		//deck: il manque Roi,Assassin,Condotière
		ArrayList<Character> deck = board.getDeckCharacter().getDeckCharacter();
		deck.remove(board.findCharacter("King"));
		deck.remove(board.findCharacter("Assassin"));
		deck.remove(board.findCharacter("Warlord"));
		assertEquals(5, deck.size());

		Character bestCharacter = richalphonse.chooseCharacterWithStrategy(richalphonse);
		
	}

	@Test
	public void testSituation25() {
		//Situation25: The Player playing third have 7districts I take Walord
		board.getDeckCharacter().getDeckCharacter().clear();
		board.getDeckCharacter().getDeckCharacter().add(new Assassin());
		board.getDeckCharacter().getDeckCharacter().add(new Bishop());
		board.getDeckCharacter().getDeckCharacter().add(new Warlord());
		Player playerWithTheLead=new Player("playerLeading");
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		playerWithTheLead.getCity().buildDistrict(new District("house",3,"red","Family"));
		board.getListOfPlayerOrdered().clear();
		board.getListOfPlayerOrdered().add(richalphonse.getPlayer());
		board.getListOfPlayerOrdered().add(new Player("test"));
		board.getListOfPlayerOrdered().add(playerWithTheLead);
		Character bestCharacter = richalphonse.chooseCharacterWithStrategy(richalphonse);
		assertEquals(richalphonse.getCurrentBestSituation().toString(),"Situation25: The Player playing third have 7districts I take Walord");
	}
	
	
}
