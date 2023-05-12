package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Optional<String[][]> oplossing = spelverdeling(6, 5, 3, 4);
        if(oplossing.isPresent()){
            printRooster(oplossing.get());
        }
        else{
            String stars = "*".repeat(100);
            System.out.println(stars);
            System.out.println("Er is geen oplossing gevonden, het aantal ploegen moet gelijk zijn aan het aantal spelletjes min één");
            System.out.println(stars);
        }
    }

    public static void printRooster(String[][] oplossing) {
        int aantalRijen = oplossing[0].length;
        int aantalKolommen = oplossing.length;

        for (int i = 0; i < aantalKolommen; i++) {
            for (int j = 0; j < aantalRijen; j++) {
                System.out.printf("%-25s", oplossing[i][j]); // Elke cel heeft een breedte van 25 karakters
            }
            System.out.println();
        }
    }


    private static ArrayList<String> getPloegen(int aantalPloegen) {
        ArrayList<String> ploegen = new ArrayList<String>();
        for (int i = 0; i < aantalPloegen; i++) {
            char team = (char)('A' + i);
            ploegen.add(Character.toString(team));
        }
        return ploegen;
    }

    private static ArrayList<String[]> getWedstrijdCombinaties(ArrayList<String> ploegen) {

        ArrayList<String[]> wedstrijdCombinaties = new ArrayList<>();
        for (int i = 0; i < ploegen.size() - 1; i++) {
            for (int j = i + 1; j < ploegen.size(); j++) {
                String[] wedstrijd = new String[]{ploegen.get(i), ploegen.get(j)};
                wedstrijdCombinaties.add(wedstrijd);
            }
        }
        return wedstrijdCombinaties;
    }

    private static ArrayList<Ronde>[] maakRondes(int aantalSpelletjes, int aantalRondes){
        ArrayList<Ronde>[] rondes = new ArrayList[aantalSpelletjes];
        for (int i = 0; i < aantalSpelletjes; i++) {
            rondes[i] = new ArrayList<Ronde>();
            for (int j = 0; j < aantalRondes ; j++){
                Ronde ronde = new Ronde(i,j,null,null);
                rondes[i].add(ronde);
            }
        }
        return rondes;
    }
    public static Optional<String[][]> spelverdeling(int aantalPloegen, int aantalSpelletjes, int dubbels, int aantalRondes){
        ArrayList<Ronde>[] voorlopigeOplossing = maakRondes(aantalSpelletjes,aantalRondes);
        int xPositie = 0;
        int yPositie = 0;
        ArrayList<String> ploegNamen= getPloegen(aantalPloegen);
        ArrayList<String[]> combinaties = getWedstrijdCombinaties(ploegNamen);
        String[] huidigeCombinatie = combinaties.get(0);
                try {
                    Optional<ArrayList<Ronde>[]> oplossing = getOplossing(ploegNamen, combinaties, aantalSpelletjes, dubbels, aantalRondes, voorlopigeOplossing, xPositie, yPositie, huidigeCombinatie);
                    if (oplossing.isPresent()) {
                        String[][] rooster = new String[aantalRondes][aantalSpelletjes];

                        for (int i = 0; i < aantalSpelletjes; i++) {
                            ArrayList<Ronde> spel = oplossing.get()[i];
                            for (int j = 0; j < aantalRondes; j++) {
                                Ronde wedstrijd = spel.get(j);
                                if (wedstrijd.getEersteTeam() != null && !wedstrijd.getEersteTeam().equals(" ")) {
                                    rooster[j][i] = wedstrijd.getEersteTeam() + " vs " + wedstrijd.getTweedeTeam();
                                } else {
                                    rooster[j][i] = "LEEG";
                                }
                            }
                        }
                        return Optional.of(rooster);
                    } else {
                        return Optional.empty();
                    }
                }catch (IndexOutOfBoundsException e){
                    return spelverdeling(aantalPloegen,aantalSpelletjes,dubbels,aantalRondes + 1);
                }
    }

    private static Optional<ArrayList<Ronde>[]> getOplossing(ArrayList<String> ploegen,ArrayList<String[]> combinaties, int aantalSpelletjes, int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie) {
        if(aantalSpelletjes != ploegen.size() - 1){
            return Optional.empty();
        }
        if(isMogelijk(dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,huidigeCombinatie, ploegen)) {
            kenDeCombinatiesToeAanDeRonde(huidigeCombinatie,voorlopigeOplossing[xPositie].get(yPositie));

            if(getDePloegenDieGeenDeelNemenAanDatSpel(xPositie, voorlopigeOplossing, ploegen).isEmpty() && xPositie == voorlopigeOplossing.length -1) {

                return Optional.of(voorlopigeOplossing);
            }
            else {
                Ronde volgendeRonde = getVolgendeRonde(aantalRondes,voorlopigeOplossing,xPositie,yPositie);
                return getOplossing(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing, volgendeRonde.getPositieX(), volgendeRonde.getPositieY(), combinaties.get(0));
            }



        }
        else{
            if(combinaties.indexOf(huidigeCombinatie) == combinaties.size()-1){
                if(yPositie == aantalRondes -1 && !getDePloegenDieGeenDeelNemenAanDatSpel(xPositie,voorlopigeOplossing,ploegen).isEmpty()) {
                    return BacktrackNaarDeVorigeRonde(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,huidigeCombinatie);
                }
                else {
                    voorlopigeOplossing[xPositie].get(yPositie).setEersteTeam(" ");
                    voorlopigeOplossing[xPositie].get(yPositie).setTweedeTeam(" ");
                    Ronde volgendeRonde = getVolgendeRonde(aantalRondes,voorlopigeOplossing,xPositie,yPositie);
                    return getOplossing(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing, volgendeRonde.getPositieX(), volgendeRonde.getPositieY(), combinaties.get(0));
                }
            }
            else{
                int huidigeCombinatieIndex = combinaties.indexOf(huidigeCombinatie);
                return getOplossing(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,combinaties.get(huidigeCombinatieIndex + 1));
            }
        }
    }

    private static void kenDeCombinatiesToeAanDeRonde(String[] huidigeCombinatie, Ronde ronde) {
        ronde.setEersteTeam(huidigeCombinatie[0]);
        ronde.setTweedeTeam(huidigeCombinatie[1]);
    }


    public static boolean isMogelijk(int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie, ArrayList<String> ploegen) {

        Set<String> ploegenInEenKolom = new HashSet<>();
        for (Ronde ronde : voorlopigeOplossing[xPositie]) {
            ploegenInEenKolom.add(ronde.getEersteTeam());
            ploegenInEenKolom.add(ronde.getTweedeTeam());
        }
        if(ploegenInEenKolom.contains(huidigeCombinatie[0]) || ploegenInEenKolom.contains(huidigeCombinatie[1]) ) return false;

        Set<String> ploegenInEenRij = new HashSet<>();
        for(ArrayList<Ronde> rondes : voorlopigeOplossing){
            ploegenInEenRij.add(rondes.get(yPositie).getEersteTeam());
            ploegenInEenRij.add(rondes.get(yPositie).getTweedeTeam());
        }
        if (ploegenInEenRij.contains(huidigeCombinatie[0]) || ploegenInEenRij.contains(huidigeCombinatie[1])) return false;




        if(!!heeftDePloegTegenDeAnderePloegGespeeld(dubbels, huidigeCombinatie[0], huidigeCombinatie[1], voorlopigeOplossing)) return false;

        if(yPositie == (aantalRondes - 1)){
            return getDePloegenDieGeenDeelNemenAanDatSpel(xPositie, voorlopigeOplossing, ploegen).size() == 2;
        }
        else{
            return true;
        }

    }
    public static ArrayList<String> getDePloegenDieGeenDeelNemenAanDatSpel(int xPositie,ArrayList<Ronde>[] voorlopigeOplossing, ArrayList<String> ploegen){
        ArrayList<String> DePloegenDieDeelNemen = new ArrayList<>();
        for (Ronde ronde : voorlopigeOplossing[xPositie]) {
            DePloegenDieDeelNemen.add(ronde.getEersteTeam());
            DePloegenDieDeelNemen.add(ronde.getTweedeTeam());
        }
        ArrayList<String> DeResterendePloegen = new ArrayList<>();
        for (String ploeg: ploegen){
            if (!DePloegenDieDeelNemen.contains(ploeg)){
                DeResterendePloegen.add(ploeg);
            }
        }
        return DeResterendePloegen;
    }

    private static Ronde getVorigeRonde(int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie) {
        if(yPositie == 0) {
            return voorlopigeOplossing[xPositie - 1 ].get(aantalRondes -1);
        }
        else {
            return voorlopigeOplossing[xPositie].get(yPositie -1);
        }
    }
    private static Ronde getVolgendeRonde(int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie) {
        if(yPositie == aantalRondes - 1) {
            return voorlopigeOplossing[xPositie + 1 ].get(0);
        }
        else {
            return voorlopigeOplossing[xPositie].get(yPositie +1);
        }
    }
    private static Optional<ArrayList<Ronde>[]> BacktrackNaarDeVorigeRonde(ArrayList<String> ploegen,ArrayList<String[]> combinaties, int aantalSpelletjes, int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie){

        voorlopigeOplossing[xPositie].get(yPositie).setEersteTeam(null);
        voorlopigeOplossing[xPositie].get(yPositie).setTweedeTeam(null);

        Ronde vorigeRonde = getVorigeRonde(aantalRondes,  voorlopigeOplossing,  xPositie,yPositie);

        if (vorigeRonde.getEersteTeam().equals(" ")){

            return BacktrackNaarDeVorigeRonde(ploegen, combinaties,  aantalSpelletjes, dubbels,  aantalRondes,  voorlopigeOplossing, vorigeRonde.getPositieX(), vorigeRonde.getPositieY(), huidigeCombinatie);
        }

        int indexCombinatiesVorigeRonde = combinaties.indexOf(huidigeCombinatie);


        if(indexCombinatiesVorigeRonde  == combinaties.size()-1){
            if(vorigeRonde.getPositieY() == aantalRondes -1 && !getDePloegenDieGeenDeelNemenAanDatSpel(xPositie,voorlopigeOplossing,ploegen).isEmpty()){
                return BacktrackNaarDeVorigeRonde(ploegen, combinaties,  aantalSpelletjes, dubbels,  aantalRondes,  voorlopigeOplossing, vorigeRonde.getPositieX(), vorigeRonde.getPositieY(), huidigeCombinatie);
            }
            else{
                voorlopigeOplossing[vorigeRonde.getPositieX()].get(vorigeRonde.getPositieY()).setEersteTeam(" ");
                voorlopigeOplossing[vorigeRonde.getPositieX()].get(vorigeRonde.getPositieY()).setTweedeTeam(" ");
                return getOplossing(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,combinaties.get(0));
            }
        }
        else {
            return getOplossing(ploegen,combinaties,aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,combinaties.get(indexCombinatiesVorigeRonde + 1));
        }
    }
    public static boolean heeftDePloegTegenDeAnderePloegGespeeld(int dubbels ,String eerstePloeg, String tweedePloeg, ArrayList<Ronde>[] voorlopigeOplossing){
        int aantalKerenGespeeld = 0;
        for (ArrayList<Ronde> rondes : voorlopigeOplossing) {
            for (Ronde ronde : rondes) {
                if(ronde.getEersteTeam() != null && ronde.getTweedeTeam() != null){
                    if(ronde.getEersteTeam().equals(eerstePloeg) && ronde.getTweedeTeam().equals(tweedePloeg) || ronde.getEersteTeam().equals(tweedePloeg) && ronde.getTweedeTeam().equals(eerstePloeg)){
                        aantalKerenGespeeld++;
                        if(aantalKerenGespeeld == dubbels){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}