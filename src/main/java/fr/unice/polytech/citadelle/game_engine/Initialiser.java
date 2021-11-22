package fr.unice.polytech.citadelle.game_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import fr.unice.polytech.citadelle.bot.Bot;
import fr.unice.polytech.citadelle.characters_class.Architect;
import fr.unice.polytech.citadelle.characters_class.Assassin;
import fr.unice.polytech.citadelle.characters_class.Bishop;
import fr.unice.polytech.citadelle.characters_class.King;
import fr.unice.polytech.citadelle.characters_class.Magician;
import fr.unice.polytech.citadelle.characters_class.Merchant;
import fr.unice.polytech.citadelle.characters_class.Thief;
import fr.unice.polytech.citadelle.characters_class.Warlord;
import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.Player;

public class Initialiser {
	
	//créer un enum
	public static final int ASSASIN_INDEX = 0;
	public static final int THIEF_INDEX = 1;
	public static final int MAGICIAN_INDEX = 2;
	public static final int KING_INDEX = 3;
	public static final int BISHOP_INDEX = 4;
	public static final int MERCHANT_INDEX = 5;
	public static final int ARCHITECT_INDEX = 6;
	public static final int WARLORD_INDEX = 7;
	public final static int NUMBER_OF_PLAYER = 4;

	
	public Initialiser() {}

	
	public void initAll(LinkedHashMap<Character, Bot> hashOfCharacters, ArrayList<Character> listOfAllCharacters, ArrayList<Bot> listOfBot, ArrayList<Player> listOfPlayer){
		initListOfAllCharacter(listOfAllCharacters);
		initHashOfCharacter(hashOfCharacters, listOfAllCharacters);
		initListOfBot(listOfBot, listOfPlayer);
	}
	
	public void initHashOfCharacter(LinkedHashMap<Character, Bot> hashOfCharacters, ArrayList<Character> listOfAllCharacters) {
		Bot fillBot = new Bot(new Player("fillPlayer"));

		hashOfCharacters.put(listOfAllCharacters.get(ASSASIN_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(THIEF_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(MAGICIAN_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(KING_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(BISHOP_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(MERCHANT_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(ARCHITECT_INDEX), fillBot);
		hashOfCharacters.put(listOfAllCharacters.get(WARLORD_INDEX), fillBot);
	}
	
	public void initListOfAllCharacter(ArrayList<Character> listOfAllCharacters) {
		Assassin theAssassin = new Assassin();
		Thief theThief = new Thief();
		Magician theMagician = new Magician();
		King theKing = new King();
		Bishop theBishop = new Bishop();
		Merchant theMerchant = new Merchant();
		Architect theArchitect = new Architect();
		Warlord theWarlord = new Warlord();

		listOfAllCharacters.add(theAssassin);
		listOfAllCharacters.add(theThief);
		listOfAllCharacters.add(theMagician);
		listOfAllCharacters.add(theKing);
		listOfAllCharacters.add(theBishop);
		listOfAllCharacters.add(theMerchant);
		listOfAllCharacters.add(theArchitect);
		listOfAllCharacters.add(theWarlord);
	}
	
	public void initListOfBot(ArrayList<Bot> listOfBot, ArrayList<Player> listOfPlayer){
		for (int i = 1; i <= NUMBER_OF_PLAYER; i++) {
			Player newPlayer = new Player("robot" + i);
			listOfBot.add(new Bot(newPlayer));
			listOfPlayer.add(newPlayer);
		}
	}
	
	public void fillHashOfCharacter(HashMap<Character, Bot> hashOfCharacters, Character character, Bot bot) {
		hashOfCharacters.put(character, bot);
	}
}