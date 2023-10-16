/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.domain;

/**
 *
 * @author Andre Kouame
 */
public class ClientFacture {
    
    private Long id;
    
    private String nomClient;
    
    
    private String telephoneClient;
    
    private String boitePostale;
    
    private Long frequenceAchat;

    public ClientFacture(Long id, String nomClient,  String telephoneClient, String boitePostale, Long frequenceAchat) {
        this.id = id;
        this.nomClient = nomClient;
        this.telephoneClient = telephoneClient;
        this.boitePostale = boitePostale;
        this.frequenceAchat = frequenceAchat;
    }
    
    public ClientFacture(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getBoitePostale() {
        return boitePostale;
    }

    public void setBoitePostale(String boitePostale) {
        this.boitePostale = boitePostale;
    }

    public Long getFrequenceAchat() {
        return frequenceAchat;
    }

    public void setFrequenceAchat(Long frequenceAchat) {
        this.frequenceAchat = frequenceAchat;
    }
    
    
}
