package fr.unice.polytech.citadelle.game_engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.Board;
import fr.unice.polytech.citadelle.game_interactor.Behaviour;
import fr.unice.polytech.citadelle.game_interactor.PhaseManager;
import fr.unice.polytech.citadelle.output.PrintCitadels;


/**
 * The controller coordinates all classes in the project
 * @author BONNET Kilian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 *
 */
public class Controller {
	private RoundManager roundManager;
	private Referee referee;

	/**
	 * Controller constructor, setting-up the printer class, the Initializer class and the PhaseManager class
	 */
	public Controller() {}

	/**
	 * Method called once to initialize the game.
	 * Initialize :
	 * - The deck of characters
	 * - The deck of districts
	 * - The game referee
	 * - The game board
	 */
	public void initGame() {
		ArrayList<Behaviour> listOfAllBehaviour;
		LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacter = new LinkedHashMap<>();
		ArrayList<Character> listOfAllCharacter = Initializer.createListOfAllCharacter();

		Board board = Initializer.createBoard(listOfAllCharacter);
		referee = new Referee(board);
		listOfAllBehaviour = Initializer.createListOfBehaviour(board);

		Initializer.initDeckCharacter(board.getDeckCharacter(), listOfAllCharacter);
		Initializer.resetHashOfCharacter(hashOfCharacter, listOfAllCharacter);

		roundManager = new RoundManager(listOfAllCharacter, listOfAllBehaviour,hashOfCharacter, board);
		
		Initializer.initDeckCharacter(roundManager.getBoard().getDeckCharacter(), listOfAllCharacter);
		Initializer.initDeckDistrict(roundManager.getBoard().getDeckDistrict());

		board.setListOfPlayer(roundManager.getListOfPlayers());

		Initializer.initPlayerCards(roundManager.getBoard());
	}

	/**
	 * Method called once to start the game after game initialization.
	 */
	public void runGame() {
		ArrayList<Behaviour> leaderBoard;
		PrintCitadels.startCitadelles();
		leaderBoard = roundManager.runRounds();
		end(leaderBoard);
	}

	/**
	 * Method called once when the game is finished. Allows the referee to calculate the game result.
	 */
	public void end(ArrayList<Behaviour> leaderBoard) {
		referee.addBonusForPlayers(leaderBoard);
		referee.getWinner();
		PrintCitadels.printRanking(roundManager.getBoard().getListOfPlayer());
	}

}
