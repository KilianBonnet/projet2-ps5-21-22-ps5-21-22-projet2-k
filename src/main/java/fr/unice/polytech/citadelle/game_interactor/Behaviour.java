package fr.unice.polytech.citadelle.game_interactor;

import java.nio.file.DirectoryStream;
import java.util.*;
import java.util.stream.Collectors;

import fr.unice.polytech.citadelle.basic_actions.BasicActions;
import fr.unice.polytech.citadelle.basic_actions.TakeGoldAction;
import fr.unice.polytech.citadelle.game.*;
import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.purple_districts.DragonGate;
import fr.unice.polytech.citadelle.game.purple_districts.HauntedCity;
import fr.unice.polytech.citadelle.game.purple_districts.SchoolOfMagic;
import fr.unice.polytech.citadelle.game_engine.PhaseManager;
import fr.unice.polytech.citadelle.output.PrintCitadels;

/**
 * A Behaviour realize all the action of a player.
 * 
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class Behaviour {
	// The player controlled by the Behaviour.
	protected final Player player;
	protected final PrintCitadels printC = new PrintCitadels();
	protected int numberOfCharacter = 8;

	// ---
	CityManagement cityMan;
	Executor executor;
	Board board;

	protected static final int ZERO_CARD = 0;
	protected static final int ONE_CARD = 1;
	protected static final int TWO_CARD = 2;

	public Behaviour(Player player, Board board) {
		this.player = player;
		cityMan = new CityManagement(player);
		executor = new Executor(player);
		this.board = board;
	}

	/**
	 * @param pickedDistricts The two picked cards.
	 * @return The district having the higher value.
	 */
	public District selectTheHigherDistrict(ArrayList<District> pickedDistricts) {
		District cardOne = pickedDistricts.get(0);
		District cardTwo = pickedDistricts.get(1);

		if (cardOne.getValue() >= cardTwo.getValue()) {
			return cardOne;
		}
		return cardTwo;
	}

	/**
	 * @param pickedDistricts The two picked cards.
	 * @return The district having the lower value.
	 */
	public District selectTheLowerDistrict(ArrayList<District> pickedDistricts) {
		DeckDistrict deckDistrict = board.getDeckDistrict();
		int cardOneValue = pickedDistricts.get(0).getValue();
		int cardTwoValue = pickedDistricts.get(1).getValue();

		if (cardOneValue < cardTwoValue) {
			deckDistrict.addDistrict(pickedDistricts.get(1));
			return pickedDistricts.get(0);
		}
		deckDistrict.addDistrict(pickedDistricts.get(0));
		return pickedDistricts.get(1);
	}

	public ArrayList<BasicActions> play(String currentPhase, LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		printC.dropALine();
		ArrayList<BasicActions> basicActions = new ArrayList<>();
		this.getPlayer().getCharacter().spellOfTurn(this, hashOfCharacters, printC);
		this.getPlayer().getCity().getBuiltDistrict().stream()
				.filter(district -> district.getName().equals("School of Magic"))
				.forEach(district -> {
					ColorDistrict schoolOfMagic = (SchoolOfMagic) district;
					schoolOfMagic.schoolOfMagicSpell(this.getPlayer());
				});
		if (currentPhase == PhaseManager.END_GAME_PHASE && player.getCity().getSizeOfCity() < 6)
			basicActions = endGameBehaviour();
		else if (currentPhase == PhaseManager.LAST_TURN_PHASE)
			basicActions = lastTurnBehaviour();
		else
			basicActions = normalBehaviour();
		buildArchitect();
		
		return basicActions;
	}

	public void buildArchitect() {
		if (player.getCharacter().getName().equals("Architect"))
			{ifPossibleBuildADistrict();
			ifPossibleBuildADistrict();}
	}

	public ArrayList<BasicActions> normalBehaviour() {
		return null;
	};

	public ArrayList<BasicActions> endGameBehaviour() {
		return null;
	};

	public ArrayList<BasicActions> lastTurnBehaviour() {
		return null;
	};


	public Character selectCharacterForSpell(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		int i = randomInt(numberOfCharacter - 1);
		Character character = (Character) hashOfCharacters.keySet().toArray()[i];
		List<Character> list = hashOfCharacters.keySet().stream().toList();
		if (this.player.getCharacter().getName().equals("Thief")) {
			character=chooseCharacterForThief(hashOfCharacters);
		}
		else if(this.player.getCharacter().getName().equals("Assassin")){
			character=chooseCharacterForAssassin(hashOfCharacters);
		}
		else if(this.player.getCharacter().getName().equals("Magician")){
			character=chooseCharacterForMagician(hashOfCharacters);
		}
		return (character);
	}

	private Character chooseCharacterForThief(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		List<Character> list = hashOfCharacters.keySet().stream().toList();
		Character randomCharacter=list.get(randomInt(8));
		while (randomCharacter.getName().equals("Assassin") || randomCharacter.getName().equals("Thief") || randomCharacter.isCharacterIsAlive() == false) {
			randomCharacter=list.get(randomInt(numberOfCharacter));
		}
		return randomCharacter;
	}


	private Character chooseCharacterForAssassin(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		List<Character> list = hashOfCharacters.keySet().stream().toList();
		Character randomCharacter=list.get(randomInt(8));
		while (randomCharacter.getName().equals("Assassin")) {
			randomCharacter=list.get(randomInt(numberOfCharacter));
		}
		return(randomCharacter);
	}

	private Character chooseCharacterForMagician(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters){
		List<Character> list = hashOfCharacters.keySet().stream().toList();
		Character randomCharacter=list.get(randomInt(numberOfCharacter));
		while (randomCharacter.getName()=="Magician")
			randomCharacter=list.get(randomInt(numberOfCharacter));
		return randomCharacter;
	}


	public void ifPossibleBuildADistrict() {
		ArrayList<HauntedCity> hauntedCityArrayList = new ArrayList<>();
		ArrayList<District> districtWeCanBuild = cityMan.listOfDistrictBuildable();
		if (!districtWeCanBuild.isEmpty()) {
			Collections.sort(districtWeCanBuild);
			Collections.reverse(districtWeCanBuild);
			District district = districtWeCanBuild.get(0);
			if(district.isA("Haunted City")) {
				HauntedCity hauntedCity = (HauntedCity) district;
				hauntedCity.setRoundBuilt(board.getRoundNumber());
			}
			executor.buildDistrict(district);
		}
	}

	public ArrayList<District> pick2CardsIntoTheDeck() {
		ArrayList<District> pickedCards = executor.pickCards(board.getDeckDistrict());
		return pickedCards;
	}

	/*
	 * For the two cards chosen look if they are present in the city or the hand, if
	 * yes we discard the card
	 */
	public ArrayList<District> chooseToKeepOrNotPickedCards(ArrayList<District> pickedDistrictCards) {
		ArrayList<District> removeDistrictCards = new ArrayList<District>();
		for (int i = 0; i < 2; i++) {
			District currentDistrictCard = pickedDistrictCards.get(i);
			if (cityMan.isAlreadyBuilt(currentDistrictCard.getName()) || player.hasDistrict(currentDistrictCard)) {
				executor.putCardBackInDeck(board.getDeckDistrict(), currentDistrictCard);
				removeDistrictCards.add(pickedDistrictCards.get(i));
			}
		}
		pickedDistrictCards.removeAll(removeDistrictCards);
		return pickedDistrictCards;
	}

	public void takeCard(District districtCard) {
		executor.takeCard(districtCard, board.getDeckDistrict());
	}

	/**
	 * Add two gold to the player.
	 * @return a takeGoldAction, that will be print with the printer
	 */
	public TakeGoldAction takeGold() {
		return executor.takeGold();
	}
	
	/**
	 * Add a district to the hand of the player.
	 * @return a addDistrictAction, that will be print with the printer
	 */
	public void addDistrict(District district){
		executor.addDistrict(district);
	}

	/**
	 * Build a district.
	 * @return a buildDistrictAction, that will be print with the printer
	 */
	public void buildDistrict(District district) {
		executor.buildDistrict(district);
	}
	
	/**
	 * Pick a districtCard into the deck.
	 * @return a pickCardAction, that will be print with the printer
	 */
	public District pickCard() {
		return(executor.pickCard(board.getDeckDistrict()));
	}

	public District pickCardsInDeck() {
		ArrayList<District> pickedCards = new ArrayList<>();
		ArrayList<District> possibleCards = new ArrayList<>();
		District choosenDistrictCard = null; // bof

		pickedCards = pick2CardsIntoTheDeck();
		possibleCards = chooseToKeepOrNotPickedCards((ArrayList<District>) pickedCards.clone());

		switch (possibleCards.size()) {
		case ONE_CARD:
			choosenDistrictCard = possibleCards.get(0);
			break;
		case TWO_CARD:
			choosenDistrictCard = chooseBetweenTwoCards(possibleCards.get(0), possibleCards.get(1));
			break;
		case ZERO_CARD:
			choosenDistrictCard = chooseBetweenTwoCards(pickedCards.get(0), pickedCards.get(1));
			break;
		}
		return choosenDistrictCard;
	}

	public District chooseBetweenTwoCards(District district, District district1) {
		return null;
	}

	public int randomInt(int scope) {
		Random random = new Random();
		return (random.nextInt(scope));
	}


	public void setCharacterIsAlive(Boolean characterIsAlive) {
		player.getCharacter().setCharacterIsAlive(characterIsAlive);
	}

	public Player getPlayer() {
		return player;
	}

	public Boolean getBehaviourIsKing() {
		return player.getCharacter().getName().equals("King");
	}

	public CityManagement getCityManager() {
		return cityMan;
	}

	public Executor getExecutor() {
		return executor;
	}


	public ArrayList<Integer> chooseMagicianAction() {
		//return an empty array if he wants to swap Cards with another Character
		//return the position of the Cards that he wants to swap
		ArrayList listOfInteger = new ArrayList();
		return(listOfInteger);
		//(for now he always chooses to steal from another Character)
	}

}
