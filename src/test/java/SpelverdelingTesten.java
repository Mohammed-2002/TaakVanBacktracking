import org.example.Ronde;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import static org.example.Main.getDePloegenDieGeenDeelNemenAanDatSpel;
import static org.example.Main.heeftDePloegTegenDeAnderePloegGespeeld;
import static org.junit.jupiter.api.Assertions.*;

public class SpelverdelingTesten {
    @Test
    void testHeeftDePloegTegenDeAnderePloegGespeeldWanneerVanWel() {
        // Arrange
        ArrayList<Ronde>[] rooster = new ArrayList[3];
        rooster[0] = new ArrayList<>();
        rooster[1] = new ArrayList<>();
        rooster[2] = new ArrayList<>();

        // Voeg ronde toe waarin 'eerstePloeg' en 'tweedePloeg' tegen elkaar spelen
        Ronde ronde1 = new Ronde(0, 0, "A", "B");
        Ronde ronde2 = new Ronde(0, 2, "C", "D");
        Ronde ronde3 = new Ronde(1, 2, "F", "H");
        Ronde ronde4 = new Ronde(2, 2, "B", "A");
        rooster[0].add(ronde1);
        rooster[2].add(ronde2);
        rooster[2].add(ronde3);
        rooster[2].add(ronde4);

        // Act
        boolean resultaat = heeftDePloegTegenDeAnderePloegGespeeld(1, "A", "B", rooster);
        // Assert
        assertTrue(resultaat);

    }

    @Test
    void testHeeftDePloegTegenDeAnderePloegGespeeldWanneerVanNiet() {
// Arrange
        ArrayList<Ronde>[] rooster = new ArrayList[3];
        rooster[0] = new ArrayList<>();
        rooster[1] = new ArrayList<>();
        rooster[2] = new ArrayList<>();

        Ronde ronde1 = new Ronde(0, 0, "A", "C");
        Ronde ronde2 = new Ronde(0, 2, "B", "D");
        Ronde ronde3 = new Ronde(1, 2, "F", "H");
        Ronde ronde4 = new Ronde(2, 2, "A", "D");
        rooster[0].add(ronde1);
        rooster[2].add(ronde2);
        rooster[2].add(ronde3);
        rooster[2].add(ronde4);


        boolean resultaat = heeftDePloegTegenDeAnderePloegGespeeld(1, "A", "B", rooster);

        assertFalse(resultaat);
    }
    @Test
    void testGetDePloegenDieGeenDeelNemenAanDatSpelWanneerDeLijstNietLeegIs() {
        // Arrange
        ArrayList<Ronde>[] rooster = new ArrayList[3];
        rooster[0] = new ArrayList<>();
        rooster[1] = new ArrayList<>();
        rooster[2] = new ArrayList<>();

        // Voeg rondes toe aan rooster
        Ronde ronde1 = new Ronde(0, 0, "A", "B");
        Ronde ronde2 = new Ronde(0, 1, "C", "D");
        Ronde ronde3 = new Ronde(2, 0, "F", "H");
        Ronde ronde4 = new Ronde(2, 1, "B", "A");
        rooster[0].add(ronde1);
        rooster[0].add(ronde2);
        rooster[2].add(ronde3);
        rooster[2].add(ronde4);

        ArrayList<String> ploegen = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));

        // Act
        ArrayList<String> resultaat = getDePloegenDieGeenDeelNemenAanDatSpel(2, rooster, ploegen);

        // Assert
        assertEquals(4, resultaat.size());
        assertTrue(resultaat.contains("E"));
        assertTrue(resultaat.contains("G"));
        assertTrue(resultaat.contains("C"));
        assertTrue(resultaat.contains("D"));
    }
    @Test
    void testGetDePloegenDieGeenDeelNemenAanDatSpelWanneerDeLijstLeegIs() {
        // Arrange
        ArrayList<Ronde>[] rooster = new ArrayList[3];
        rooster[0] = new ArrayList<>();
        rooster[1] = new ArrayList<>();
        rooster[2] = new ArrayList<>();

        // Voeg rondes toe aan rooster
        Ronde ronde1 = new Ronde(0, 0, "A", "B");
        Ronde ronde2 = new Ronde(0, 2, "C", "D");
        Ronde ronde3 = new Ronde(0, 3, "E", "F");
        Ronde ronde4 = new Ronde(0, 4, "G", "H");
        Ronde ronde5 = new Ronde(2, 0, "F", "H");
        Ronde ronde6 = new Ronde(2, 1, "B", "A");
        rooster[0].add(ronde1);
        rooster[0].add(ronde2);
        rooster[0].add(ronde3);
        rooster[0].add(ronde4);
        rooster[2].add(ronde5);
        rooster[2].add(ronde6);

        ArrayList<String> ploegen = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));

        // Act
        ArrayList<String> resultaat = getDePloegenDieGeenDeelNemenAanDatSpel(0, rooster, ploegen);

        // Assert
        assertEquals(0, resultaat.size());
    }


}



