package com.webstocker.web.rest.reports.wrapper;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * Created by komi on 29/07/16.
 */
public class EtatVenteParMagasinWrapper {


    String typeSortie;
    String nomMagasin;

    Date dateDebutPeriode;
    Date dateFinPeriode;

    public String getTypeSortie() {
        return typeSortie;
    }

    public void setTypeSortie(String typeSortie) {
        this.typeSortie = typeSortie;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public Date getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(Date dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public Date getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(Date dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }
}
