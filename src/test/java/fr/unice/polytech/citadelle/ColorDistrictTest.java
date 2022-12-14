package fr.unice.polytech.citadelle;

import fr.unice.polytech.citadelle.game.*;

import fr.unice.polytech.citadelle.game.purple_districts.SchoolOfMagic;
import fr.unice.polytech.citadelle.game_character.*;
import fr.unice.polytech.citadelle.game_character.Character;
import fr.unice.polytech.citadelle.game.purple_districts.HauntedCity;
import fr.unice.polytech.citadelle.game.purple_districts.Laboratory;
import fr.unice.polytech.citadelle.game_engine.Initializer;
import fr.unice.polytech.citadelle.game_engine.Referee;
import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Behaviour;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import fr.unice.polytech.citadelle.output.PrintCitadels;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ColorDistrictTest {
    Referee referee = new Referee(new Board());
    Board board = new Board(new ArrayList<Player>(), new ArrayList<Character>(), new DeckDistrict(), new DeckCharacter(4));

    @BeforeEach
    void init(){
        PrintCitadels.activateLevelWarning();
    }


    @RepeatedTest(1)
    //@Test
    //Haunted City built before the last round
    void isThereHauntedCityJokerInCityTest() {
        Player player = new Player("Player");
        player.buildDistrict(new Laboratory("Laboratory", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Cathedral",5,"Blue","Religion"));
        player.buildDistrict(new HauntedCity("Haunted City", 2,"Purple","Prestige", -1));
        player.buildDistrict(new District("Fortress",5,"Red","Soldiery"));
        player.buildDistrict(new District("Town Hall",5,"Green","Trade and Handicrafts"));

        ArrayList<HauntedCity> hauntedCityArrayList = new ArrayList<>();
        hauntedCityArrayList.add(new HauntedCity("Haunted City", 2,"Purple","Prestige", 3));

        assertEquals(hauntedCityArrayList, referee.isThereHauntedCityJokerInCity(player));
    }

    @RepeatedTest(1)
    //@Test
    //Haunted City built on the last round
    void isThereHauntedCityJokerInCityTest2() {
        Player player = new Player("Player");
        player.buildDistrict(new Laboratory("Laboratory", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Cathedral",5,"Blue","Religion"));
        player.buildDistrict(new HauntedCity("Haunted City", 2,"Purple","Prestige", 0));
        player.buildDistrict(new District("Fortress",5,"Red","Soldiery"));
        player.buildDistrict(new District("Town Hall",5,"Green","Trade and Handicrafts"));

        ArrayList<HauntedCity> hauntedCityArrayList = new ArrayList<>();

        assertEquals(hauntedCityArrayList, referee.isThereHauntedCityJokerInCity(player));
    }

    @RepeatedTest(1)
    //@Test
    //No Haunted City in the city
    void isThereHauntedCityJokerInCityTest3() {
        Player player = new Player("Player");
        player.buildDistrict(new Laboratory("Laboratory", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Cathedral",5,"Blue","Religion"));
        player.buildDistrict(new District("Fortress",5,"Red","Soldiery"));
        player.buildDistrict(new District("Town Hall",5,"Green","Trade and Handicrafts"));

        ArrayList<HauntedCity> hauntedCityArrayList = new ArrayList<>();

        assertEquals(hauntedCityArrayList, referee.isThereHauntedCityJokerInCity(player));
    }

    @RepeatedTest(1)
    //@Test
    //Haunted City in the city but no other Purple
    void isThereHauntedCityJokerInCityTest4() {
        Player player = new Player("Player");
        player.buildDistrict(new District("Cathedral",5,"Blue","Religion"));
        player.buildDistrict(new HauntedCity("Haunted City", 2,"Purple","Prestige", -1));
        player.buildDistrict(new District("Fortress",5,"Red","Soldiery"));
        player.buildDistrict(new District("Town Hall",5,"Green","Trade and Handicrafts"));

        ArrayList<HauntedCity> hauntedCityArrayList = new ArrayList<>();

        assertEquals(hauntedCityArrayList, referee.isThereHauntedCityJokerInCity(player));
    }

    @RepeatedTest(1)
    //@Test
    //Test if the Haunted City spell does its job
    void hauntedCitySpellTest() {
        ArrayList<String> colorsInCity = new ArrayList<>();

        ArrayList<String> existingColors = new ArrayList<>();
        existingColors.add("Blue");
        existingColors.add("Yellow");
        existingColors.add("Green");
        existingColors.add("Red");
        existingColors.add("Purple");

        Player player = new Player("Player");
        player.buildDistrict(new Laboratory("Laboratory", 5,"Purple","Prestige"));
        player.buildDistrict(new District("Cathedral",5,"Blue","Religion"));
        player.buildDistrict(new HauntedCity("Haunted City", 2,"Purple","Prestige", -1));
        player.buildDistrict(new District("Fortress",5,"Red","Soldiery"));
        player.buildDistrict(new District("Town Hall",5,"Green","Trade and Handicrafts"));

        ColorDistrict colorDistrict = new ColorDistrict(null,0, null, null);
        colorDistrict.hauntedCitySpell(player);

        for(District district : player.getCity().getBuiltDistrict()) colorsInCity.add(district.getColor());

        for(String color : existingColors) {
            assertTrue(colorsInCity.contains(color));
        }

        assertEquals("Yellow", player.getCity().getBuiltDistrict().get(2).getColor());
    }

    @RepeatedTest(1)
    //@Test
    //Test if the School of Magic spell does its job
    void schoolOfMagicSpellTest() {
        Player player = new Player("Player");
        player.buildDistrict(new SchoolOfMagic("School of Magic", 6,"Purple","Prestige"));
        player.setGolds(0);

        SchoolOfMagic schoolOfMagic = (SchoolOfMagic) player.getCity().getBuiltDistrict().get(0);

        //Must add one gold
        player.setRole(new King());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(1,player.getGolds());

        //Must add one gold
        player.setRole(new Bishop());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(2,player.getGolds());

        //Do nothing
        player.setRole(new Assassin());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(2,player.getGolds());

        //Must add one gold
        player.setRole(new Merchant());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(3,player.getGolds());

        //Do nothing
        player.setRole(new Thief());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(3,player.getGolds());

        //Must add one gold
        player.setRole(new Warlord());
        schoolOfMagic.schoolOfMagicSpell(player);
        assertEquals(4,player.getGolds());
    }

    @RepeatedTest(1)
    //@Test
    //Test on play() method from Behavior with King
    void playKTest() {
        Character character = new King();
        Behaviour behavior = spy(new Behaviour(new Player("testPlayer"), board));

        behavior.getPlayer().setRole(character);

        behavior.getPlayer().buildDistrict(new SchoolOfMagic("School of Magic", 6,"Purple","Prestige"));
        behavior.getPlayer().setGolds(0);

        behavior.play(Mockito.any());
        assertEquals(1, behavior.getPlayer().getGolds());
    }

    @RepeatedTest(1)
    //@Test
    //Test on play() method from Behavior with Bishop
    void playBTest() {
        Character character = new Bishop();
        Behaviour behavior = spy(new Behaviour(new Player("testPlayer"), board));

        behavior.getPlayer().setRole(character);

        behavior.getPlayer().buildDistrict(new SchoolOfMagic("School of Magic", 6,"Purple","Prestige"));
        behavior.getPlayer().setGolds(0);

        behavior.play(Mockito.any());
        assertEquals(1, behavior.getPlayer().getGolds());
    }

    @RepeatedTest(1)
    //@Test
    //Test on play() method from Behavior with Merchant
    void playMTest() {
        Character character = new Merchant();
        Behaviour behavior = spy(new Behaviour(new Player("testPlayer"), board));

        behavior.getPlayer().setRole(character);

        behavior.getPlayer().buildDistrict(new SchoolOfMagic("School of Magic", 6,"Purple","Prestige"));
        behavior.getPlayer().setGolds(0);

        behavior.play(Mockito.any());
        assertEquals(2, behavior.getPlayer().getGolds());
        //2 golds because the merchant earn one gold at the beginning of his turn
        //So one gold from the beginning of the turn plus one gold from the spell of the district
    }

    @RepeatedTest(1)
    //@Test
    //Test on play() method from Behavior with Warlord
    void playWTest() {
        board = Initializer.createBoard(Initializer.createListOfAllCharacter(), 4);
        Character character = new Warlord();

        Player alice = new Player("alice");

        board.getListOfPlayer().add(alice);

        Behaviour behavior = spy(new Behaviour(alice, board));

        behavior.getPlayer().setRole(character);

        behavior.getPlayer().buildDistrict(new SchoolOfMagic("School of Magic", 6,"Purple","Prestige"));
        behavior.getPlayer().setGolds(0);

        behavior.play(Mockito.any());
        assertEquals(1, behavior.getPlayer().getGolds());
    }

}