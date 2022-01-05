package fr.unice.polytech.citadelle.game_interactor.game_strategy;

import java.util.ArrayList;
import java.util.stream.Collectors;

import fr.unice.polytech.citadelle.game.Board;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_character.Character;


public class SituationLibrairie {
	private ArrayList<Situation> librairieContent;
	private Board board;

	//SituationInitializer.generateAllSituations(board);

	 
	public SituationLibrairie(Board board) {
		this.librairieContent = new  ArrayList<Situation>();
		this.board = board;
	}
	
	
	
	public ArrayList<Situation> filterByListOfCharacterPickable(ArrayList<Situation> listOfSituation, ArrayList<Character> listOfRichardCharacterPickable) {
		return listOfSituation.stream()
								.filter(situation ->situation.getListOfCharacterPickable().isEmpty() || listOfRichardCharacterPickable.containsAll(situation.getListOfCharacterPickable().get())										)
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByListOfCharacterNotPickable(ArrayList<Situation> listOfSituation, ArrayList<Character> listOfRichardCharacterNotPickable) {
		return listOfSituation.stream()
								.filter(situation -> situation.getListOfCharacterNotHere().isEmpty() || doNotContain(listOfRichardCharacterNotPickable, listOfRichardCharacterNotPickable))
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByPositionOfPotentialWinner(ArrayList<Situation> listOfSituation, boolean currentBestPlayerPlayFirst) {
		return listOfSituation.stream()
								.filter(situation -> situation.getaPlayerCloseToWinPlayFirst().isEmpty() || situation.getaPlayerCloseToWinPlayFirst().get() == currentBestPlayerPlayFirst)
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByOrderOfPlay(ArrayList<Situation> listOfSituation, int orderOfPlay){
		return listOfSituation.stream().
								filter(situation -> situation.getPlayOrder().isEmpty() || situation.getPlayOrder().get() == orderOfPlay).
								collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByPlayerCloseToWinPlayFirst(ArrayList<Situation> listOfSituation, boolean currentBestPlayerPlayFirst) {
		return listOfSituation.stream()
								.filter(situation -> situation.getaPlayerCloseToWinPlayFirst().isEmpty() || situation.getaPlayerCloseToWinPlayFirst().get() == currentBestPlayerPlayFirst)
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByDistanceBetweenRichardAndAPlayerCloseToWin(ArrayList<Situation> listOfSituation, int currentDdistanceBetweenRichardAndAPlayerCloseToWin) {
		return listOfSituation.stream()
								.filter(situation -> situation.getDistanceBetweenRichardAndAPlayerCloseToWin().isEmpty() || situation.getDistanceBetweenRichardAndAPlayerCloseToWin().get() == currentDdistanceBetweenRichardAndAPlayerCloseToWin)
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Situation> filterByIHave6Districts(ArrayList<Situation> listOfSituation, boolean RichardHas6Districts) {
		return listOfSituation.stream()
								.filter(situation -> situation.getiHave6Districts().isEmpty() || situation.getiHave6Districts().get() == RichardHas6Districts)
								.collect(Collectors.toCollection(ArrayList::new));
	}
	
	
	
		
	private boolean doNotContain(ArrayList<Character> list1, ArrayList<Character> list2) {
		for(Character character : list1){
			if(list2.contains(character)) return false;
		}
		return true;
	}


	public ArrayList<Situation> getLibrairieContent() {
		if(librairyEmpty()) this.librairieContent = SituationInitializer.generateAllSituations(board);
		return librairieContent;
	}
	
	private boolean librairyEmpty() {
		if(librairieContent.size() == 0) return true;
		return false;
	}
	
	
	
}
