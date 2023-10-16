package com.webstocker.utilitaires;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Athanase
 */
public class PremierEtDernierJourDuMois {

    private String dateDuMois;

    public PremierEtDernierJourDuMois(String dateDuMois) {
        this.dateDuMois = dateDuMois;
    }

    public PremierEtDernierJourDuMois() {

    }

    public String getDateDuMois() {
        return dateDuMois;
    }

    public void setDateDuMois(String dateDuMois) {
        this.dateDuMois = dateDuMois;
    }

    public int getMois(String dateDuMois) {
        int numeroMois = 0;
        String jour;
        String annee;
        String mois;

        annee = dateDuMois.substring(0, 4);
        mois = dateDuMois.substring(5, 7);
        jour = dateDuMois.substring(8, 10);

        numeroMois = Integer.parseInt(mois);
        return numeroMois;
    }

    public int getJour(String dateDuMois) {
        int numeroJour = 0;
        String jour;
        String annee;
        String mois;

        annee = dateDuMois.substring(0, 4);
        mois = dateDuMois.substring(5, 7);
        jour = dateDuMois.substring(8, 10);

        numeroJour = Integer.parseInt(jour);
        return numeroJour;
    }

    public int getAnnee(String dateDuMois) {
        int numeroAnnee = 0;
        String jour;
        String annee;
        String mois;

        annee = dateDuMois.substring(0, 4);
        mois = dateDuMois.substring(5, 7);
        jour = dateDuMois.substring(8, 10);

        numeroAnnee = Integer.parseInt(jour);
        return numeroAnnee;
    }

    /**
     * Retourne la dernière date du mois de la date passée en paramètre
     *
     * @param dateDuMois
     * @return
     */
    public String getDateFinDuMois(String dateDuMois) {
        String dateFin = "";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        Date dm = null;
        Calendar cal = Calendar.getInstance();

        try {
            d = dateFormat.parse(dateDuMois);
            cal.setTime(d);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            dm = cal.getTime();

        } catch (ParseException ex) {
            Logger.getLogger(PremierEtDernierJourDuMois.class.getName()).log(Level.SEVERE, null, ex);
        }
        dateFin = dateFormat.format(dm);
        System.out.println("FIN DU MOIS : " + dateFin);
        return dateFin;
    }

    /**
     * retourne la date du début du mois de la date passée en paramètre
     *
     * @param dateDuMois
     * @return
     */
    public String getDateDebutDuMois(String dateDuMois) {
        String dateDebut = "";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        Date dm = null;
        Calendar cal = Calendar.getInstance();

        try {
            d = dateFormat.parse(dateDuMois);
            cal.setTime(d);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            dm = cal.getTime();

        } catch (ParseException ex) {
            Logger.getLogger(PremierEtDernierJourDuMois.class.getName()).log(Level.SEVERE, null, ex);
        }
        dateDebut = dateFormat.format(dm);
        System.out.println("DEBUT DU MOIS : " + dateDebut);
        return dateDebut;
    }

    public String getDateDebutAnnee() {

        String dateDebutAnnee;
        
        String mois = "01";
        String jour = "01";
        int y = 0;
        
        LocalDate mad = LocalDate.now();
        y = mad.getYear();

        dateDebutAnnee = y + "-" + mois + "-" + jour;
        System.out.println("LA DATE DEBUT ANNEE : " + dateDebutAnnee);

        return dateDebutAnnee;
    }

    public String getDateFinAnnee() {

        String dateFinAnnee;
    
        String mois = "12";
        String jour = "31";
        int y = 0;
       
        LocalDate mad = LocalDate.now();
        y = mad.getYear();
       
        dateFinAnnee = y + "-" + mois + "-" + jour;
        System.out.println("LA DATE FIN ANNEE : " + dateFinAnnee);
        return dateFinAnnee;
    }

}
