package com.webstocker.service.newfeature;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class CalendrierApprovisionnementService {

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;
    @Inject
    ProduitRepository produitRepository;
    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    public List<EtatStockGlobalAimasWrapper> etatRecapitulatifVenteEtPromo(String dateDebut, String dateFin) {
        final List<EtatStockGlobalAimasWrapper> listRecapitulatif = new ArrayList<>();
        Long stockDebutMois = 0L;
        Long stockTheoriqueFinal = 0L;
        Long stockReelFinal = 0L;
        Long stockReelMagasinCentral = 0L;
        Long stockReelCommerciaux = 0L;
        Long distributionMoyenMensuelle = 0L;
        Long stockReelAntenne = 0L;
        Long couverture = 0L;
        Long quantiteVendu = 0L;
        Long quantitePomo = 0L;
        Long quantitePerte = 0L;


        final String dateCommenecement = "2016-12-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate debut = LocalDate.parse(dateDebut, formatter);
        final LocalDate fin = LocalDate.parse(dateFin, formatter);
        final LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<LigneBonDeSortie> sorteDebutaFin = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, fin);


        return null;
    }

}
