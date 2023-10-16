package com.webstocker.reports;

import com.webstocker.domain.Objectifs;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;

/**
 *
 * @author Athanase
 */
public class TauxAtteinteObjectifWrapper {
    String dateObjectif;
    String nomProduit;
    Long quantiteAttendue;
    Long quantiteObtenue;
    Double taux;
    String Janvier;
    String Fevrier;
    String Mars;
    String Avril;
    String Mai;
    String Juin;
    String Juillet;
    String Aout;
    String Septembre;
    String Octobre;
    String Novembre;
    String Decembre;

    public String getJanvier() {
        return Janvier;
    }

    public void setJanvier(String Janvier) {
        this.Janvier = Janvier;
    }

    public String getFevrier() {
        return Fevrier;
    }

    public void setFevrier(String Fevrier) {
        this.Fevrier = Fevrier;
    }

    public String getMars() {
        return Mars;
    }

    public void setMars(String Mars) {
        this.Mars = Mars;
    }

    public String getAvril() {
        return Avril;
    }

    public void setAvril(String Avril) {
        this.Avril = Avril;
    }

    public String getMai() {
        return Mai;
    }

    public void setMai(String Mai) {
        this.Mai = Mai;
    }

    public String getJuin() {
        return Juin;
    }

    public void setJuin(String Juin) {
        this.Juin = Juin;
    }

    public String getJuillet() {
        return Juillet;
    }

    public void setJuillet(String Juillet) {
        this.Juillet = Juillet;
    }

    public String getAout() {
        return Aout;
    }

    public void setAout(String Aout) {
        this.Aout = Aout;
    }

    public String getSeptembre() {
        return Septembre;
    }

    public void setSeptembre(String Septembre) {
        this.Septembre = Septembre;
    }

    public String getOctobre() {
        return Octobre;
    }

    public void setOctobre(String Octobre) {
        this.Octobre = Octobre;
    }

    public String getNovembre() {
        return Novembre;
    }

    public void setNovembre(String Novembre) {
        this.Novembre = Novembre;
    }

    public String getDecembre() {
        return Decembre;
    }

    public void setDecembre(String Decembre) {
        this.Decembre = Decembre;
    }
    
    

    public String getDateObjectif() {
        int m = 0;
        String nomMois;
        PremierEtDernierJourDuMois pdj = new PremierEtDernierJourDuMois();
        m= pdj.getMois(dateObjectif);
        switch(m){
            case 1 :
                nomMois ="Janvier";
                break;
            case 2 :
                 nomMois="Février";
                break;
            case 3 :
                 nomMois="Mars";
                break;
            case 4 :
                 nomMois="Avril";
                break;
            case 5 :
                 nomMois="Mai";
                break;
            case 6 :
                 nomMois="Juin";
                break;
            case 7 :
                 nomMois="Juillet";
                return nomMois;
            case 8 :
                 nomMois="Août";
                break;
            case 9 :
                 nomMois="Septembre";
                 break;
            case 10 :
                 nomMois="Octobre";
                break;
            case 11 :
                 nomMois="Novembre";
               
                break;
            case 12 :
                 nomMois="Décembre";
                
                break;
               default:
                 return nomMois= dateObjectif; 
        }
            
       
        return nomMois;
    }

    public void setDateObjectif(String dateObjectif) {
        this.dateObjectif = dateObjectif;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getQuantiteAttendue() {
        return quantiteAttendue;
    }

    public void setQuantiteAttendue(Long quantiteAttendue) {
        this.quantiteAttendue = quantiteAttendue;
    }

    public Long getQuantiteObtenue() {
        return quantiteObtenue;
    }

    public void setQuantiteObtenue(Long quantiteObtenue) {
        this.quantiteObtenue = quantiteObtenue;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }
    
    public TauxAtteinteObjectifWrapper(Objectifs objectif){
       this.dateObjectif = objectif.getPeriode().toString();
       this.nomProduit = objectif.getProduit().getNomProduit();
       this.quantiteAttendue = objectif.getQuantiteAttendue();
       this.quantiteObtenue = objectif.getQuantiteObtenu();
       this.taux = Double.valueOf(objectif.getTaux());
       this.Janvier = objectif.getPeriode().toString();
       this.Mars = objectif.getPeriode().toString();
       this.Fevrier = objectif.getPeriode().toString();
       this.Avril = objectif.getPeriode().toString();
       this.Juin = objectif.getPeriode().toString();
       this.Juillet = objectif.getPeriode().toString();
       this.Mai = objectif.getPeriode().toString();
       this.Aout = objectif.getPeriode().toString();
       this.Octobre = objectif.getPeriode().toString();
       this.Septembre = objectif.getPeriode().toString();
       this.Novembre = objectif.getPeriode().toString();
       this.Decembre = objectif.getPeriode().toString();
    }
    
    
    
}
