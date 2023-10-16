package com.webstocker.service.impl;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lot;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.LotRepository;
import com.webstocker.repository.search.FactureSearchRepository;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.search.BonDeSortieSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
    public BonDeSortie saveVente(BonDeSortie bonDeSortie, Integer delaiPaiement, Long remise, LocalDate dateFacture) {
        log.debug("Request to save BonDeSortie : {}", bonDeSortie);
        Long ilot=0L;
        Long ilot2=0L;
        int nb =0;
        Set<LigneBonDeSortie> ligneBonDeSorties = bonDeSortie.getLigneBonDeSorties();
        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ilot = ligneBonDeSortie.getLot().getId();
//            if()
            Lot lot = ligneBonDeSortie.getLot();
            lot.setQuantiteSortie(ligneBonDeSortie.getQuantite());            
            lotRepository.save(lot);
        }

        BonDeSortie result = bonDeSortieRepository.save(bonDeSortie);
        bonDeSortieSearchRepository.save(result);

        Facture fact = factureRepository.findByBonDeSortie(result);

        if (fact != null) {
            factureRepository.delete(fact.getId());
            factureSearchRepository.delete(fact.getId());

            if (factureRepository.exists(fact.getId())) {
                System.out.println("Cette Facture : id " + fact + " existe deja");
            } else {
                Facture facture = new Facture();
                facture.setBonDeSortie(result);
                if (remise == null) {
                    remise = 0L;
                }
                facture.setValeurRemise(remise.intValue());
                System.out.println("LA REMISE " + remise);
                facture.setDateFacture(dateFacture);

                if (bonDeSortie.getTypeVente().equals(TypeVente.CREDIT)) {
                    LocalDate datePaiement = dateFacture.plusDays(delaiPaiement.longValue());
                    facture.setDateLimitePaiement(datePaiement);
                }

                facture.setClient(result.getClient());
                log.debug("Request to save Facture : {}", facture);
                Facture save = factureRepository.save(facture);
                factureSearchRepository.save(save);
            }
        } else {

            Facture facture = new Facture();
            facture.setBonDeSortie(result);
            if (remise == null) {
                remise = 0L;
            }
            facture.setValeurRemise(remise.intValue());
            System.out.println("LA REMISE " + remise);
            facture.setDateFacture(dateFacture);

            if (bonDeSortie.getTypeVente().equals(TypeVente.CREDIT)) {
                LocalDate datePaiement = dateFacture.plusDays(delaiPaiement.longValue());
                facture.setDateLimitePaiement(datePaiement);
            }
            
            facture.setClient(result.getClient());
            log.debug("Request to save Facture : {}", facture);
            Facture save = factureRepository.save(facture);
            factureSearchRepository.save(save);
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
        log.debug("Request to get all BonDeSorties");
        Page<BonDeSortie> result = bonDeSortieRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BonDeSortie> findAll() {
        log.debug("Request to get all BonDeSorties");
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
        BonDeSortie bonDeSortie = bonDeSortieRepository.findOne(id);
        return bonDeSortie;
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
        log.debug("Request to get all BonDeSorties");
        Page<BonDeSortie> page = bonDeSortieRepository.findBonByNumber(numero, pageable);
        return page;
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
        Page<BonDeSortie> page = bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
        return page;
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortiePromotion(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.PROMOTION;
        Page<BonDeSortie> page = bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
        return page;
    }
    /**
     * 
     * Retourne tous les bon de sortie de promotion
     * @return 
     */
    @Override
    public List<BonDeSortie> listeBonDeSortiePromotion() {
        TypeSortie typeSortie = TypeSortie.PROMOTION;
        List<BonDeSortie> promotion = bonDeSortieRepository.findByTypeSortie(typeSortie);
        return promotion;
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortieTransfert(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.TRANSFERT;
        Page<BonDeSortie> page = bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
        return page;
    }

    @Override
    public Page<BonDeSortie> listeBonDeSortiePerte(Pageable pageable) {
        TypeSortie typeSortie = TypeSortie.PERTE;
        Page<BonDeSortie> page = bonDeSortieRepository.findByTypeSortie(typeSortie, pageable);
        return page;
    }

    @Override
    public Page<BonDeSortie> retrouverBonDeSortieVenteParNumero(String numero, Pageable pageable) {
        Page<BonDeSortie> page = bonDeSortieRepository.findByTypeSortieAndNumeroContaining(TypeSortie.VENTE, numero, pageable);
        return page;
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
                System.out.println("Cette Facture : id " + fact + " existe deja");
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
    public Page<BonDeSortie> transfertEncours( Pageable pageable) {
        return bonDeSortieRepository.findByStatusTranfertOrderByDaateCreation(StatusTransfert.ENCOURS, pageable);
    }

}
