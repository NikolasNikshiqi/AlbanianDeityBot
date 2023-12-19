package com.AlbaninanDeity.Entities.Football;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match {

    private LocalDateTime utcDate;
    private LocalDateTime lastUpdated;
    private String status;
    private Team homeTeam;
    private Team awayTeam;


    public Match(LocalDateTime utcDate, LocalDateTime lastUpdated, String status) {
        this.utcDate = utcDate;
        this.lastUpdated = lastUpdated;
        this.status = status;
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

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getFormattedDate() {
        // Check if utcDate is not null before formatting
        if (utcDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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
}
