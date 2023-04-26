package org.example;

public class Ronde {
    private int positieX;
    private int positieY;
    private String eersteTeam;

    private String tweedeTeam;



    public Ronde(int positieX, int positieY, String eersteTeam, String tweedeTeam) {
        this.positieX = positieX;
        this.positieY = positieY;
        this.eersteTeam = eersteTeam;
        this.tweedeTeam = tweedeTeam;

    }

    public int getPositieX() {
        return positieX;
    }

    public int getPositieY() {
        return positieY;
    }

    public String getEersteTeam() {
        return eersteTeam;
    }

    public String getTweedeTeam() {
        return tweedeTeam;
    }

    public void setEersteTeam(String eersteTeam) {
        this.eersteTeam = eersteTeam;
    }

    public void setTweedeTeam(String tweedeTeam) {
        this.tweedeTeam = tweedeTeam;
    }
    public int getAantalGevuldeTeams(){
        int aantal = 0;
        if(eersteTeam != null){
            aantal++;
        }
        if(tweedeTeam != null){
            aantal++;
        }
        return aantal;
    }
}

