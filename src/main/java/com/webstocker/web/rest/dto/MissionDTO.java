/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.web.rest.dto;

import com.webstocker.domain.LigneBudget;
import com.webstocker.domain.LigneMissionActivite;
import com.webstocker.domain.Mission;
import java.util.List;

/**
 *
 * @author Andre Kouame
 */
public class MissionDTO {
    private Mission mission;
    private List<LigneBudget>ligneBudgets;
    private List<LigneMissionActivite> ligneMissionActivites;

    public MissionDTO() {
    }
    
    
    

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public List<LigneBudget> getLigneBudgets() {
        return ligneBudgets;
    }

    public void setLigneBudgets(List<LigneBudget> ligneBudgets) {
        this.ligneBudgets = ligneBudgets;
    }

    public List<LigneMissionActivite> getLigneMissionActivites() {
        return ligneMissionActivites;
    }

    public void setLigneMissionActivites(List<LigneMissionActivite> ligneMissionActivites) {
        this.ligneMissionActivites = ligneMissionActivites;
    }
    
    
    
    
}
