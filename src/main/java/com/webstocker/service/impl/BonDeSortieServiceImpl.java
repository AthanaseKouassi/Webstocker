package com.webstocker.service.impl;

import com.webstocker.domain.*;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import com.webstocker.repository.*;
import com.webstocker.repository.search.BonDeSortieSearchRepository;
import com.webstocker.repository.search.FactureSearchRepository;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.ReglementService;
import com.webstocker.utilitaires.WebstockerConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing BonDeSortie.
 */
@Service
@Transactional
public class BonDeSortieServiceImpl implements BonDeSortieService {

    private final Logger log = LoggerFactory.getLogger(BonDeSortieServiceImpl.class);

    @Inject
    private BonDeSortieRepository bonDeSortieRepository;

    @Inject
    private FactureRepository factureRepository;

    @Inject
    private FactureSearchRepository factureSearchRepository;

    @Inject
    private BonDeSortieSearchRepository bonDeSortieSearchRepository;

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private LotRepository lotRepository;
    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;
    @Autowired
    private ReglementService reglementService;

    /**
     * Save a bonDeSortie.
     *
     * @param bonDeSortie the entity to save
     * @return the persisted entity
     */
    public BonDeSortie save(BonDeSortie bonDeSortie) {
        log.debug("Request to save BonDeSortie : {}", bonDeSortie);
        BonDeSortie result = bonDeSortieRepository.save(bonDeSortie);
        bonDeSortieSearchRepository.save(result);
        return result;
    }

    @Override
    public BonDeSortie saveVente(BonDeSortie bonDeSortie, Integer delaiPaiement, Long remise, LocalDate dateFacture
        , LocalDate dateReglement) {

        log.debug("Demande d'enregistrement du BonDeSortie : {}", bonDeSortie);

        Set<LigneBonDeSortie> ligneBonDeSorties = bonDeSortie.getLigneBonDeSorties();

        ligneBonDeSorties.forEach(ligneBonDeSortie -> {
            Lot lot = ligneBonDeSortie.getLot();
            lot.setQuantiteSortie(ligneBonDeSortie.getQuantite());
            lotRepository.save(lot);
        });

        BonDeSortie result = bonDeSortieRepository.save(bonDeSortie);
        bonDeSortieSearchRepository.save(result);

        Facture fact = factureRepository.findByBonDeSortie(result);

        if (fact != null) {
            deleteFacture(fact);
        }

        Facture facture = createFacture(result, remise, dateFacture, delaiPaiement);

        factureRepository.save(facture);
        factureSearchRepository.save(facture);
        if (TypeVente.CASH.equals(result.getTypeVente())) {
            reglementService.reglementFacture(facture, dateReglement.format(
                DateTimeFormatter.ofPattern(WebstockerConstant.FORMAT_DATE)));
        }


        return result;
    }

    /**
     * Get all the bonDeSorties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BonDeSortie> findAll(Pageable pageable) {
        return bonDeSortieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BonDeSortie> findAll() {
        return bonDeSortieRepository.findAll();
    }

    /**
     * Get one bonDeSortie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BonDeSortie findOne(Long id) {
        log.debug("Request to get BonDeSortie : {}", id);
        return bonDeSortieRepository.findOne(id);
    }

    /**
     * Delete the bonDeSortie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BonDeSortie : {}", id);
        bonDeSortieRepository.delete(id);
        bonDeSortieSearchRepository.delete(id);
    }

    /**
     * Search for the bonDeSortie corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BonDeSortie> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BonDeSorties for query {}", query);
        return bonDeSortieSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<BonDeSortie> bonEtBordereauParMagasin(String nomMagasin, String dateDebut, String dateFin) {

        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return bonDeSortieRepository.findByMagasinNomMagasinAndDaateCreationBetween(magasin.getNomMagasin(), debut, fin);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BonDeSortie> findBonByNumero(String numero, Pageable pageable) {
        return bonDeSortieRepository.findBonByNumber(numero, pageable);
    }

    @Override
    public List<BonDeSortie> listeTransfertParMagasin(String nomMagasin, String dateDebut, String dateFin) {

        TypeSortie typesortie = TypeSortie.TRANSFERT;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return bonDeSortieRepository.findByMagasinNomMagasinAndTypeSortieAndDaateCreationBetween(nomMagasin, typesortie, debut, fin);
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortieVente(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.VENTE;
        return bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortiePromotion(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.PROMOTION;
        return bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
    }

    /**
     * Retourne tous les bon de sortie de promotion
     *
     * @return
     */
    @Override
    public List<BonDeSortie> listeBonDeSortiePromotion() {
        TypeSortie typeSortie = TypeSortie.PROMOTION;
        return bonDeSortieRepository.findByTypeSortie(typeSortie);
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortieTransfert(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.TRANSFERT;
        return bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortiePerte(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.PERTE;
        return bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
    }

    @Override
    public Page<BonDeSortie> retrouverBonDeSortieVenteParNumero(String numero, Pageable pageable) {
        return bonDeSortieRepository.findByTypeSortieAndNumeroContaining(TypeSortie.VENTE, numero, pageable);
    }

    @Override
    public Page<BonDeSortie> retrouverBonDeSortiePromotionParNumero(String numero, Pageable pageable) {
        return bonDeSortieRepository.findByTypeSortieAndNumeroContaining(TypeSortie.PROMOTION, numero, pageable);
    }

    @Override
    public Page<BonDeSortie> retrouverBonDeSortieTRansfertParNumero(String numero, Pageable pageable) {
        return bonDeSortieRepository.findByTypeSortieAndNumeroContaining(TypeSortie.TRANSFERT, numero, pageable);
    }

    @Override
    public Page<BonDeSortie> retrouverBonDeSortiePerteParNumero(String numero, Pageable pageable) {
        return bonDeSortieRepository.findByTypeSortieAndNumeroContaining(TypeSortie.PERTE, numero, pageable);
    }

    @Override
    public BonDeSortie savePromotion(BonDeSortie bonDeSortie, LocalDate dateFacture) {

        Set<LigneBonDeSortie> ligneBonDeSorties = bonDeSortie.getLigneBonDeSorties();
        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            Lot lot = ligneBonDeSortie.getLot();
            lot.setQuantiteSortie(ligneBonDeSortie.getQuantite());
            lotRepository.save(lot);
        }

        BonDeSortie result = bonDeSortieRepository.save(bonDeSortie);
        bonDeSortieSearchRepository.save(result);

        Facture fact = factureRepository.findByBonDeSortie(result);

        if (fact != null) {
            /*Supprimer d'abord la ligne facture  avant de la reecrire fact.getId() */

            factureRepository.delete(fact.getId());
            factureSearchRepository.delete(fact.getId());

            if (factureRepository.exists(fact.getId())) {
                log.info("Cette Facture : id {}  existe deja", fact);
            } else {

                /*Reecris la ligne de facture supprimée fact.getId()*/
                Facture facture = new Facture();
                facture.setBonDeSortie(result);
                facture.setDateFacture(dateFacture);
                facture.setClient(result.getClient());
                log.debug("Request to save Facture : {}", facture);
                Facture save = factureRepository.save(facture);
                factureSearchRepository.save(save);
            }
        } else {
            Facture facture = new Facture();
            facture.setBonDeSortie(result);
            facture.setDateFacture(dateFacture);
            facture.setClient(result.getClient());
            log.debug("Request to save Facture : {}", facture);
            Facture save = factureRepository.save(facture);
            factureSearchRepository.save(save);
        }
        return result;
    }

    @Override
    public Page<BonDeSortie> transfertEncours(Pageable pageable) {
        return bonDeSortieRepository.findByStatusTranfertOrderByDaateCreation(StatusTransfert.ENCOURS, pageable);
    }
    //***************************** NOUVEAUX CODES 2023 : AJOUT NOUVELLES FONCTIONNALITES *********************/
    //********************** REFACTORISATION ************


    /**
     * méthode supprimer une facture
     *
     * @param facture
     */
    private void deleteFacture(Facture facture) {
        factureRepository.delete(facture.getId());
        if (factureRepository.exists(facture.getId())) {
            log.warn("La Facture avec l'ID {} existe déjà.", facture.getId());
        }
    }

    private Facture createFacture(BonDeSortie result, Long remise, LocalDate dateFacture, Integer delaiPaiement) {
        Facture facture = new Facture();
        facture.setBonDeSortie(result);
        facture.setValeurRemise(remise != null ? remise.intValue() : 0);
        facture.setDateFacture(dateFacture);

        if (TypeVente.CREDIT.equals(result.getTypeVente())) {
            LocalDate datePaiement = dateFacture.plusDays(delaiPaiement);
            facture.setDateLimitePaiement(datePaiement);
        }

        facture.setClient(result.getClient());
        facture.setStatutFacture(StatutFacture.NON_SOLDE);
        log.debug("Facture ID: {} créé ", facture.getId());
        return facture;
    }
}
