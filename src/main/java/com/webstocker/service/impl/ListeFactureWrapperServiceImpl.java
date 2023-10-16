package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Client;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.ListeFactureWrapper;
import com.webstocker.reports.ListeDesFactureWrapper;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.ClientRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.service.ListeFactureWrapperService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Athanase
 */
@Service
@Transactional
public class ListeFactureWrapperServiceImpl implements ListeFactureWrapperService {

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    ClientRepository clientRepository;

    @Inject
    BonDeSortieRepository bonDeSortieRepository;

    /**
     * renvoie les factures de la date dateDebut à la date dateFin
     *
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @Override
    public List<ListeFactureWrapper> trouverFactureAunePeriode(String dateDebut, String dateFin) {
//        BigDecimal montantfacture = BigDecimal.ZERO;
        Long montantfacture = 0L;

        String nomClient = null;
        String dateFacture = null;
        String numeroFacture = null;
        String numeroBs = null;

        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ListeFactureWrapper> listeDesFacture = new LinkedList<>();

        //Liste des bon de sortie de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(TypeSortie.VENTE, debut, fin);

        List<BonDeSortie> listeBonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> itBonDesortie = listeBonDeSortie.iterator();
        while (itBonDesortie.hasNext()) {

            BonDeSortie bonDeSortie = itBonDesortie.next();
            if (bonDeSortie.getTypeSortie().equals(TypeSortie.VENTE)) {

                nomClient = bonDeSortie.getClient().getNomClient();
                dateFacture = bonDeSortie.getFacture() != null ? bonDeSortie.getFacture().getDateFacture().toString() : "";
                numeroFacture = bonDeSortie.getNumeroFactureNormalise();

                for (LigneBonDeSortie l : listeSortieParPeriode) {
                    if (l.getBonDeSortie().getClient() != null) {
                        if (l.getBonDeSortie().getClient().getNomClient().equals(nomClient)
                                && l.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                                && l.getBonDeSortie().getNumeroFactureNormalise().equals(numeroFacture)) {
                            montantfacture  += l.getPrixDeVente();
                            
                        }
                    }
                }

                ListeFactureWrapper listeFactureWrapper = new ListeFactureWrapper();
                listeFactureWrapper.setNomClient(nomClient);
                listeFactureWrapper.setDateFacture(dateFacture);
                listeFactureWrapper.setBonDeSortie(bonDeSortie);

                listeFactureWrapper.setMontantTotal(montantfacture);
                montantfacture = 0L;
                nomClient = null;
                dateFacture = null;
                numeroFacture = null;
                numeroBs = null;

                listeDesFacture.add(listeFactureWrapper);
            }
        }
        return listeDesFacture;
    }

    @Override
    public List<ListeFactureWrapper> trouverFactureParNumeroFacture(String numeroFacture) {

//        BigDecimal montantfacture = BigDecimal.ZERO;
        Long montantfacture = 0L;
//        BigDecimal mt = BigDecimal.ZERO;
        String nomClient = null;
        String dateFacture = null;

        String numeroBs = null;

        List<ListeFactureWrapper> listeDesFacture = new LinkedList<>();

        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieNumeroFactureNormaliseContaining(TypeSortie.VENTE, numeroFacture);

        List<BonDeSortie> listeBonDeSortie = bonDeSortieRepository.findByTypeSortieAndNumeroFactureNormaliseContaining(TypeSortie.VENTE, numeroFacture);
        Iterator<BonDeSortie> itBonDesortie = listeBonDeSortie.iterator();
        while (itBonDesortie.hasNext()) {
            BonDeSortie bonDeSortie = itBonDesortie.next();
            if (bonDeSortie.getTypeSortie().equals(TypeSortie.VENTE)) {

                nomClient = bonDeSortie.getClient().getNomClient();
                dateFacture = bonDeSortie.getFacture() != null ? bonDeSortie.getFacture().getDateFacture().toString() : "";
                numeroFacture = bonDeSortie.getNumeroFactureNormalise();

                for (LigneBonDeSortie l : listeSortieParPeriode) {
                    if (l.getBonDeSortie().getClient() != null) {
                        if (l.getBonDeSortie().getClient().getNomClient().equals(nomClient)
                                && l.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                                && l.getBonDeSortie().getNumeroFactureNormalise().equals(numeroFacture)) {
                            montantfacture += l.getPrixDeVente();                            
                        }
                    }
                }

                ListeFactureWrapper listeFactureWrapper = new ListeFactureWrapper();
                listeFactureWrapper.setNomClient(nomClient);
                listeFactureWrapper.setDateFacture(dateFacture);
                listeFactureWrapper.setBonDeSortie(bonDeSortie);

                listeFactureWrapper.setMontantTotal(montantfacture);
                montantfacture = 0L;                
                nomClient = null;
                dateFacture = null;
                numeroFacture = null;
                numeroBs = null;

                listeDesFacture.add(listeFactureWrapper);
            }
        }
        return listeDesFacture;

    }

    @Override
    public List<ListeFactureWrapper> creanceClients(String dateCreance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListeFactureWrapper> trouverFactureZeroParPeriode(String dateDebut, String dateFin) {
//        BigDecimal montantfacture = BigDecimal.ZERO;
        Long montantfacture = 0L;
        String nomClient = null;
        String dateFacture = null;
        String numeroFacture = null;
        String numeroBs = null;

        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ListeFactureWrapper> listeDesFacture = new LinkedList<>();

        //Liste des bon de sortie de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(TypeSortie.PROMOTION, debut, fin);

        List<BonDeSortie> listeBonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> itBonDesortie = listeBonDeSortie.iterator();
        while (itBonDesortie.hasNext()) {

            BonDeSortie bonDeSortie = itBonDesortie.next();
            if (bonDeSortie.getTypeSortie().equals(TypeSortie.PROMOTION)) {

                nomClient = bonDeSortie.getClient().getNomClient();
                dateFacture = bonDeSortie.getFacture() != null ? bonDeSortie.getFacture().getDateFacture().toString() : "";
                numeroFacture = bonDeSortie.getNumeroFactureNormalise();

                for (LigneBonDeSortie l : listeSortieParPeriode) {
                    if (l.getBonDeSortie().getClient() != null) {
                        if (l.getBonDeSortie().getClient().getNomClient().equals(nomClient)
                                && l.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                                && l.getBonDeSortie().getNumeroFactureNormalise().equals(numeroFacture)) {
                            montantfacture = l.getPrixDeVente();
                        }
                    }
                }

                ListeFactureWrapper listeFactureWrapper = new ListeFactureWrapper();
                listeFactureWrapper.setNomClient(nomClient);
                listeFactureWrapper.setDateFacture(dateFacture);
                listeFactureWrapper.setBonDeSortie(bonDeSortie);

                listeFactureWrapper.setMontantTotal(montantfacture);
                montantfacture = 0L;
                nomClient = null;
                dateFacture = null;
                numeroFacture = null;
                numeroBs = null;

                listeDesFacture.add(listeFactureWrapper);
            }
        }
        return listeDesFacture;
    }

    @Override
    public List<ListeFactureWrapper> trouverFactureZeroParNumeroFacture(String numeroFacture) {
//        BigDecimal montantfacture = BigDecimal.ZERO;
        Long montantfacture = 0L;
        String nomClient = null;
        String dateFacture = null;

        String numeroBs = null;

        List<ListeFactureWrapper> listeDesFacture = new LinkedList<>();

        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieNumeroFactureNormaliseContaining(TypeSortie.PROMOTION, numeroFacture);

        List<BonDeSortie> listeBonDeSortie = bonDeSortieRepository.findByTypeSortieAndNumeroFactureNormaliseContaining(TypeSortie.PROMOTION, numeroFacture);
        Iterator<BonDeSortie> itBonDesortie = listeBonDeSortie.iterator();
        while (itBonDesortie.hasNext()) {
            BonDeSortie bonDeSortie = itBonDesortie.next();
            if (bonDeSortie.getTypeSortie().equals(TypeSortie.PROMOTION)) {

                nomClient = bonDeSortie.getClient().getNomClient();
                dateFacture = bonDeSortie.getFacture() != null ? bonDeSortie.getFacture().getDateFacture().toString() : "";
                numeroFacture = bonDeSortie.getNumeroFactureNormalise();

                for (LigneBonDeSortie l : listeSortieParPeriode) {
                    if (l.getBonDeSortie().getClient() != null) {
                        if (l.getBonDeSortie().getClient().getNomClient().equals(nomClient)
                                && l.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                                && l.getBonDeSortie().getNumeroFactureNormalise().equals(numeroFacture)) {
                            montantfacture = l.getPrixDeVente();
                        }
                    }
                }

                ListeFactureWrapper listeFactureWrapper = new ListeFactureWrapper();
                listeFactureWrapper.setNomClient(nomClient);
                listeFactureWrapper.setDateFacture(dateFacture);
                listeFactureWrapper.setBonDeSortie(bonDeSortie);

                listeFactureWrapper.setMontantTotal(montantfacture);
                montantfacture = 0L;
                nomClient = null;
                dateFacture = null;
                numeroFacture = null;
                numeroBs = null;

                listeDesFacture.add(listeFactureWrapper);
            }
        }
        return listeDesFacture;

    }

}
