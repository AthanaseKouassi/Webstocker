package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.repository.search.ReglementSearchRepository;
import com.webstocker.service.ReglementService;
import com.webstocker.service.util.WebstockerDateFormat;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import com.webstocker.web.rest.dto.newfeature.ReglementDto;
import com.webstocker.web.rest.dto.newfeature.ReglementFactureDto;
import com.webstocker.web.rest.mapper.newfeature.DetailFactureMapper;
import com.webstocker.web.rest.mapper.newfeature.ReglementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Reglement.
 */
@Service
@Transactional
public class ReglementServiceImpl implements ReglementService {

    private final Logger log = LoggerFactory.getLogger(ReglementServiceImpl.class);
    @Inject
    private BonDeSortieRepository bonDeSortieRepository;

    @Inject
    private ReglementRepository reglementRepository;
    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;
    @Inject
    private ReglementSearchRepository reglementSearchRepository;
    @Inject
    private FactureRepository factureRepository;
    @Inject
    private WebstockerDateFormat webstockerDateFormat;
    @Autowired
    private DetailFactureMapper detailFactureMapper;
    @Autowired
    private ReglementMapper reglementMapper;


    public Reglement save(Reglement reglement) {
        log.debug("Request to save Reglement : {}", reglement);
        return reglementRepository.save(reglement);
    }

    /**
     * Get all the reglements.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reglement> findAll() {
        log.debug("Request to get all Reglements");
        return reglementRepository.findAll();
    }

    /**
     * Get one reglement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Reglement findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        return reglementRepository.findOne(id);
    }

    /**
     * Delete the  reglement by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        reglementRepository.delete(id);
    }

    /**
     * Search for the reglement corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reglement> search(String query) {
        log.debug("Request to search Reglements for query {}", query);
        return StreamSupport
            .stream(reglementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Reglement> getTousLesReglementDuMois(String maDate) {

        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return reglementRepository.findByDateReglementBetween(debut, fin);
    }

    @Override
    public List<Reglement> findByFacture(Facture facture) {
        return reglementRepository.findByFacture(facture);
    }

    @Override
    public void reglementFacture(Facture facture, String dateReglement) {
        BonDeSortie bonDeSortie = bonDeSortieRepository.findOne(facture.getBonDeSortie().getId());
        if (bonDeSortie != null && "CASH".equals(bonDeSortie.getTypeVente().toString())) {
            reglementFactureCash(bonDeSortie, facture, dateReglement);
        }
    }

    @Override
    public ReglementFactureDto reglementFactureCredit(Long idFacture, List<Reglement> reglements) {
        ReglementFactureDto reglementFactureDto = new ReglementFactureDto();
        Facture facture = factureRepository.findOne(idFacture);
        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieRepository.findAllByBonDeSortie(facture.getBonDeSortie());
        List<Reglement> listReglementsEffectue = new ArrayList<>();

        List<Boolean> listbool = new ArrayList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSorties) {

            double sumMontDejaRegle = 0;
            if (lbs.getBonDeSortie().getFacture().getReglements() != null) {
                sumMontDejaRegle = lbs.getBonDeSortie()
                    .getFacture()
                    .getReglements()
                    .stream().filter(r -> Objects.equals(r.getProduit().getId(), lbs.getProduit().getId()))
                    .mapToDouble(Reglement::getMontantReglement)
                    .sum();
            }

            Reglement reg = reglements.stream()
                .filter(m -> Objects.equals(m.getProduit().getId(), lbs.getProduit().getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Aucun élément correspondant trouvé"));
            log.info("LA SOMME DEJA REGLE POUR LE PRODUIT {} EST :: {}", reg.getProduit().getId(), sumMontDejaRegle);
            log.info("LE MONTANT REGLE POUR LE PRODUIT {} EST :: {}", reg.getProduit().getId(), reg.getMontantReglement());

            double totalRegle = sumMontDejaRegle + (reg.getMontantReglement() != null ? reg.getMontantReglement() : 0L);
            if (totalRegle <= Double.valueOf(lbs.getPrixDeVente()) && reg.getMontantReglement() != null) {
                Reglement reglement = new Reglement();
                reglement.setProduit(reg.getProduit());
                reglement.setFacture(facture);
                reglement.setDateReglement(reg.getDateReglement());
                reglement.setMontantReglement(reg.getMontantReglement());

                listReglementsEffectue.add(reglement);
                listbool.add(totalRegle == Double.valueOf(lbs.getPrixDeVente()));
                log.info("TOTAL REGLE POUR {}  est :: {}", reg.getProduit().getId(), totalRegle);
                log.info("MONTANT DE LA VENTE POUR {} est :: {}", reg.getProduit().getId(), Double.valueOf(lbs.getPrixDeVente()));
            }
        }

        reglementRepository.save(listReglementsEffectue);
        if (listbool.stream().allMatch(Boolean::booleanValue)) {
            facture.setStatutFacture(StatutFacture.SOLDE);
            factureRepository.save(facture);
        }

        reglementFactureDto.setIdFacture(idFacture);
        List<ReglementDto> listReglement = listReglementsEffectue.stream().map(r -> {
            ReglementDto reglementDto = new ReglementDto();
            reglementDto.setIdFacture(r.getFacture().getId());
            reglementDto.setMontantReglement(r.getMontantReglement());
            reglementDto.setDateReglement(r.getDateReglement());
            reglementDto.setIdProduit(r.getProduit().getId());
            return reglementDto;
        }).collect(Collectors.toList());

        reglementFactureDto.setReglementDtos(listReglement);
        return reglementFactureDto;
    }

    private void reglementFactureCash(BonDeSortie bonDeSortie, Facture facture, String dateReglement) {

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
        for (LigneBonDeSortie lbs : ligneBonDeSorties) {
            Reglement reglement = new Reglement();
            reglement.setProduit(lbs.getProduit());
            reglement.setFacture(facture);
            reglement.setDateReglement(webstockerDateFormat.convertirStingToLocalDate(dateReglement));
            reglement.setMontantReglement(lbs.getPrixDeVente());

            reglementRepository.save(reglement);
            factureRepository.updateStatutFacture(StatutFacture.SOLDE.toString(), facture.getId());
        }
    }


}
