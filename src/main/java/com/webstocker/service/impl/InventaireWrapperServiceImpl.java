package com.webstocker.service.impl;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.domain.wrapper.InventaireWrapper;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.service.EtatDeTousLesProduitsDunMagasinService;
import com.webstocker.service.EtatStockGlobalService;
import com.webstocker.service.InventaireWrapperService;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing InventaireWrapper.
 *
 * @author Athanase
 */
@Service
@Transactional
public class InventaireWrapperServiceImpl implements InventaireWrapperService {

    private final Logger log = LoggerFactory.getLogger(InventaireWrapperServiceImpl.class);

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private EtatDeTousLesProduitsDunMagasinService etatDeTousLesProduitsDunMagasinService;

    @Inject
    private EtatStockGlobalService etatStockGlobalService;

    @Override
    public List<InventaireWrapper> situationStockMagasin(String nomProduit, String nomMagasin, String dateInventaire) {

        return null;
    }

    @Override
    public InventaireWrapper situationDunProduitMagasin(String nomProduit, String nomMagasin, String dateInventaire) {
        String dateDebutPeriode;
        String dateFinPeriode;

        Long quantiteStock = 0L;
        Long stockInit = 0L;
        //Classe retournant la première et la dernière date du mois de la date données en paramètre : dateInventaire
        PremierEtDernierJourDuMois madateInventaire = new PremierEtDernierJourDuMois();
        dateDebutPeriode = madateInventaire.getDateDebutDuMois(dateInventaire);
        dateFinPeriode = madateInventaire.getDateFinDuMois(dateInventaire);

        InventaireWrapper invent = new InventaireWrapper();

        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        List<EtatDeTousLesProduitsDunMagasinWrapper> edtlms = etatDeTousLesProduitsDunMagasinService.etatdeTousLesProduitDunMagasin(magasin, dateDebutPeriode, dateFinPeriode);

        Iterator<EtatDeTousLesProduitsDunMagasinWrapper> itList = edtlms.iterator();


        while (itList.hasNext()) {
            EtatDeTousLesProduitsDunMagasinWrapper etp = itList.next();

            if (etp.getProduit().getNomProduit().equals(nomProduit)) {
                invent.setQuantiteTheorique(etp.getQuantiteTotalEnStock());
                invent.setDateInventaire(dateInventaire);
                invent.setNomMagasin(nomMagasin);
                invent.setNomProduit(nomProduit);
                stockInit = etp.getQuantiteTotalEnStock() + etp.getQuantiteVendue() + etp.getQuantitePromo() + etp.getQuantitePerte() + etp.getQuantiteTransfert() - etp.getQuantiteTransfertRecuMagasin();
                invent.setStockInitial(etp.getStockInitial());

            }
            stockInit = 0L;
        }

        return invent;

    }

    @Override
    public InventaireWrapper getEtatProduit(String nomProduit, String dateInventaire) {
        InventaireWrapper inventaire = new InventaireWrapper();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateInventaireLocal = LocalDate.parse(dateInventaire, formatter);

        LocalDate startOfMonth = dateInventaireLocal.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = dateInventaireLocal.with(TemporalAdjusters.lastDayOfMonth());

        List<EtatStockGlobalAimasWrapper> listStockGlobalProduit = etatStockGlobalService.etatStockGlobalNew(startOfMonth, endOfMonth);

        Optional<EtatStockGlobalAimasWrapper> result = listStockGlobalProduit.stream()
            .filter(stock -> nomProduit.equals(stock.getProduit().getNomProduit()))
            .findFirst();

        if (result.isPresent()) {
            final EtatStockGlobalAimasWrapper etatGlobal = result.get();
            inventaire.setNomProduit(etatGlobal.getProduit().getNomProduit());
            inventaire.setQuantiteTheorique(etatGlobal.getQuantiteTotalEnStock());
            inventaire.setArrivage(etatGlobal.getArrivage());
            inventaire.setVente(etatGlobal.getQuantiteVendue());
            inventaire.setPromo(etatGlobal.getQuantitePromotion());
            inventaire.setPerteAbime(etatGlobal.getQuantitePerte());

        }

        return inventaire;
    }

}
