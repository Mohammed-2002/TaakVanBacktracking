package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ArrayList<Ronde>[] arry = spelverdeling(6, 5, 1, 6);
        printRooster(arry);
    }

    public static void printRooster(ArrayList<Ronde>[] arry) {
        int rows = arry[0].size(); // Aantal rijen is gelijk aan het aantal wedstrijden in de eerste ronde
        int cols = arry.length; // Aantal kolommen is gelijk aan het aantal rondes

        // Maak een 2D-array om het rooster op te slaan
        String[][] rooster = new String[rows][cols];

        // Vul het rooster met de namen van de teams
        for (int rondeNummer = 0; rondeNummer < cols; rondeNummer++) {
            ArrayList<Ronde> ronde = arry[rondeNummer];
            for (int i = 0; i < rows; i++) {
                Ronde wedstrijd = ronde.get(i);
                rooster[i][rondeNummer] = wedstrijd.getEersteTeam() + " vs " + wedstrijd.getTweedeTeam();
            }
        }

        // Print het rooster
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(String.format("%-25s", rooster[i][j])); // Elke cel heeft een breedte van 25 karakters
            }
            System.out.println();
        }
    }


    public static ArrayList<String> getPloegen(int aantalPloegen) {
        ArrayList<String> ploegen = new ArrayList<String>();
        for (int i = 0; i < aantalPloegen; i++) {
            char team = (char)('A' + i);
            ploegen.add(Character.toString(team));
        }
        return ploegen;
    }

    public static ArrayList<String[]> getWedstrijdCombinaties(ArrayList<String> ploegen) {
        ArrayList<String[]> wedstrijdCombinaties = new ArrayList<>();

        for (int i = 0; i < ploegen.size() - 1; i++) {
            for (int j = i + 1; j < ploegen.size(); j++) {
                String[] wedstrijd = new String[]{ploegen.get(i), ploegen.get(j)};
                wedstrijdCombinaties.add(wedstrijd);
            }
        }
        return wedstrijdCombinaties;
    }

    public static ArrayList<Ronde>[] maakRondes(int aantalSpelletjes, int aantalRondes){
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
    public static ArrayList<Ronde>[] spelverdeling(int aantalPloegen, int aantalSpelletjes, int dubbels, int aantalRondes){
        ArrayList<Ronde>[] voorlopigeOplossing = maakRondes(aantalSpelletjes,aantalRondes);
        int xPositie = 0;
        int yPositie = 0;
        ArrayList<String> ploegNamen= getPloegen(aantalPloegen);
        ArrayList<String[]> combinaties = getWedstrijdCombinaties(ploegNamen);
        String[] huidigeCombinatie = combinaties.get(0);
            try{
                return getOplossing(ploegNamen, combinaties, aantalSpelletjes, dubbels, aantalRondes, voorlopigeOplossing, xPositie, yPositie,huidigeCombinatie);
            }
            catch (ArrayIndexOutOfBoundsException e){
                return spelverdeling(aantalPloegen,aantalSpelletjes,dubbels,aantalRondes);
            }
    }

    private static ArrayList<Ronde>[] getOplossing(ArrayList<String> ploegen,ArrayList<String[]> combinaties, int aantalSpelletjes, int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie) {
        if(isMogelijk(combinaties, aantalSpelletjes,dubbels,aantalRondes,voorlopigeOplossing,xPositie,yPositie,huidigeCombinatie, ploegen)) {
            kenDeCombinatiesToeAanDeRonde(huidigeCombinatie,voorlopigeOplossing[xPositie].get(yPositie));

            if(getDePloegenDieGeenDeelNemenAanDatSpel(xPositie, voorlopigeOplossing, ploegen).isEmpty() && xPositie == voorlopigeOplossing.length -1) {

                return voorlopigeOplossing;
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


    private static boolean isMogelijk( ArrayList<String[]> combinaties, int aantalSpelletjes, int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie, ArrayList<String> ploegen) {

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




        if(!heeftDePloegTegenDeAnderePloegGespeeld(huidigeCombinatie[0], huidigeCombinatie[1], voorlopigeOplossing) == false) return false;

        if(yPositie == (aantalRondes - 1)){
            if(getDePloegenDieGeenDeelNemenAanDatSpel(xPositie,voorlopigeOplossing,ploegen).size() == 2){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return true;
        }

    }
    private static ArrayList<String> getDePloegenDieGeenDeelNemenAanDatSpel(int xPositie,ArrayList<Ronde>[] voorlopigeOplossing, ArrayList<String> ploegen){
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
    private static ArrayList<Ronde>[] BacktrackNaarDeVorigeRonde(ArrayList<String> ploegen,ArrayList<String[]> combinaties, int aantalSpelletjes, int dubbels, int aantalRondes, ArrayList<Ronde>[] voorlopigeOplossing, int xPositie, int yPositie, String[] huidigeCombinatie){

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
    private static boolean heeftDePloegTegenDeAnderePloegGespeeld(String eerstePloeg, String tweedePloeg, ArrayList<Ronde>[] voorlopigeOplossing){
        for (ArrayList<Ronde> rondes : voorlopigeOplossing) {
            for (Ronde ronde : rondes) {
                if(ronde.getEersteTeam() != null && ronde.getTweedeTeam() != null){
                    if(ronde.getEersteTeam().equals(eerstePloeg) && ronde.getTweedeTeam().equals(tweedePloeg)){
                        return true;
                    }
                    if(ronde.getEersteTeam().equals(tweedePloeg) && ronde.getTweedeTeam().equals(eerstePloeg)){
                        return true;
                    }

                }
            }
        }
        return false;
    }


}