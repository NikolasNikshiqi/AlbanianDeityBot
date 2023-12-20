package com.AlbaninanDeity.Entities.Football;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match {

    private LocalDateTime utcDate;
    private LocalDateTime lastUpdated;
    private String status;
    private Team homeTeam;
    private Team awayTeam;

    private Integer homeScore;

    private Integer awayScore;


    public Match(LocalDateTime utcDate, LocalDateTime lastUpdated, String status) {
        this.utcDate = utcDate;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.homeScore = null;
        this.awayScore = null;
    }

    public LocalDateTime getUtcDate() {
        return utcDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public String getFormattedDate() {
        // Check if utcDate is not null before formatting
        if (utcDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return utcDate.format(formatter);
        } else {
            return "N/A"; // or any other default value for null dates
        }
    }

    public String getOpponents() {
        String teams = homeTeam.getName() + " -vs- " + awayTeam.getName();
        return teams;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public String getScore(){
        if (this.homeScore != null) {
            return "" + this.homeScore + " - " + this.awayScore;
        }else {
            return " ";
        }
    }


    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
}
