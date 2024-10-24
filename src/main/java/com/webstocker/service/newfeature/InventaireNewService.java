package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.exception.IllegalArgumentException;
import com.webstocker.exception.InvalideDateFormatException;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatStockGlobalService;
import com.webstocker.utilitaires.Constantes;
import com.webstocker.utilitaires.Utils;
import com.webstocker.web.rest.dto.newfeature.InventaireDto;
import com.webstocker.web.rest.dto.newfeature.InventairePagineDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class InventaireNewService {

    private static final String PATTERN_MOIS = "MMMM yyyy";

    @Inject
    private InventaireRepository inventaireRepository;
    @Inject
    private Utils utils;
    @Inject
    private ProduitRepository produitRepository;
    @Inject
    private EtatStockGlobalService etatStockGlobalService;


    public List<Inventaire> getInventaireByMonth(String dateInventaire) {

        if (dateInventaire.isEmpty()) {
            throw new IllegalArgumentException("La date d'inventaire ne peut pas être nulle ou vide");
        }
        try {
            final LocalDate startOfMonth = utils.getStartOfMonth(dateInventaire);
            final LocalDate endOfMonth = utils.getEndOfMonth(dateInventaire);
            log.info("Date début du mois: {} et date fin du mois: {}", startOfMonth, endOfMonth);

            return inventaireRepository.findByDateInventaireBetween(startOfMonth, endOfMonth);
        } catch (DateTimeParseException e) {
            throw new InvalideDateFormatException("Le format de la date est invalide : " + dateInventaire, e);
        }

    }

    public Map<String, List<Inventaire>> allInventairesfirstMonthToActuallyOfYear(String dateInventaire) {
        Map<String, List<Inventaire>> mapList = new HashMap<>();
        final List<LocalDate> listmonth = utils.getMonthsAndYearsUpToDate(dateInventaire);

        Locale locale = Locale.FRENCH;

        for (LocalDate month : listmonth) {
            final String monthText = month.format(DateTimeFormatter.ofPattern(PATTERN_MOIS, locale));
            final List<Inventaire> listInventaireMensuels = inventaireRepository
                .findByDateInventaireBetween(month.with(TemporalAdjusters.firstDayOfMonth()),
                    month.with(TemporalAdjusters.lastDayOfMonth()));

            mapList.put(monthText, listInventaireMensuels);
        }

        return mapList;
    }


    public Map<Produit, List<Inventaire>> getAllInventaireParProduit(String dateInventaire) {

        final Map<Produit, List<Inventaire>> mapInventaire = new HashMap<>();
        final List<Produit> lstProduit = produitRepository.findAll();
        final DateTimeFormatter format = DateTimeFormatter.ofPattern(Constantes.PATTERN_DATE);
        final LocalDate dateLocalInventaire = LocalDate.parse(dateInventaire, format);
        final int year = dateLocalInventaire.getYear();

        for (Produit produit : lstProduit) {
            final List<Inventaire> inventaires = inventaireRepository.findByInventaireByYearAndProduit(year, produit);

            if (!inventaires.isEmpty()) {
                mapInventaire.put(produit, inventaires);
            }
        }
        return mapInventaire;
    }

    public Inventaire getInventaireByProduitAndDate(Long idProduit, String dateInventaire) {
        final Produit produit = produitRepository.findOne(idProduit);
        Inventaire inventaire = new Inventaire();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate dateInventaireLocal = LocalDate.parse(dateInventaire, formatter);
        final LocalDate startOfMonth = dateInventaireLocal.with(TemporalAdjusters.firstDayOfMonth());
        final LocalDate endOfMonth = dateInventaireLocal.with(TemporalAdjusters.lastDayOfMonth());

        final List<Inventaire> lstInventaire = inventaireRepository.findByDateInventaireBetween(startOfMonth, endOfMonth);

        Optional<Inventaire> result = lstInventaire.stream()
            .filter(i -> i.getProduit().equals(produit))
            .findFirst();

        if (result.isPresent()) {
            final long ajustement = (result.get().getStockTheoDebut() + result.get().getArrivage())
                - result.get().getVente() - result.get().getPromo() - result.get().getPerteAbime()
                - (result.get().getStockMagasinCentral() + result.get().getStockAgent() + result.get().getStockAntenne());

            inventaire.setProduit(result.get().getProduit());
            inventaire.setDateInventaire(result.get().getDateInventaire());
            inventaire.setStockTheoDebut(result.get().getStockTheoDebut());
            inventaire.setArrivage(result.get().getArrivage());
            inventaire.setBailleur(result.get().getBailleur());
            inventaire.setPerteAbime(result.get().getPerteAbime());
            inventaire.setStockAgent(result.get().getStockAgent());
            inventaire.setId(result.get().getId());
            inventaire.setStockAntenne(result.get().getStockAntenne());
            inventaire.setVente(result.get().getVente());
            inventaire.setPromo(result.get().getPromo());
            inventaire.setAjustement(ajustement);
            inventaire.setStockMagasinCentral(result.get().getStockMagasinCentral());
        }

        return inventaire;
    }

    public List<Inventaire> getInventaireparAnneeAndProduit(int year, Long idProduit) {
        if (year <= 0 || idProduit == null) {
            throw new IllegalArgumentException("Argument Invalide");
        }
        final Produit produit = produitRepository.findOne(idProduit);
        return inventaireRepository.findByInventaireByYearAndProduit(year, produit);
    }

    public InventaireDto getInventaireById(Long idInventaire) {

        final Inventaire inventaire = inventaireRepository.findOne(idInventaire);
        final long ajustement = (inventaire.getStockTheoDebut() + inventaire.getArrivage())
            - inventaire.getVente() - inventaire.getPromo() - inventaire.getPerteAbime()
            - (inventaire.getStockAgent() + inventaire.getStockMagasinCentral() + inventaire.getStockAgent());

        InventaireDto inventaireDto = getInventaireDto(inventaire);
        inventaireDto.setAjustement(ajustement);

        return inventaireDto;
    }

    public InventairePagineDto getInventaireByPage(Pageable pageable) {

        final Page<Inventaire> inventairePage = inventaireRepository.findAll(pageable);
        InventairePagineDto inventairePagineDto = new InventairePagineDto();

        inventairePagineDto.setInventaires(inventairePage.getContent());  // Les objets paginés
        inventairePagineDto.setTotalElements(inventairePage.getTotalElements());  // Nombre total d'éléments
        inventairePagineDto.setTotalPages(inventairePage.getTotalPages());  // Nombre total de pages
        inventairePagineDto.setCurrentPage(inventairePage.getNumber());  // Page actuelle
        inventairePagineDto.setPageSize(inventairePage.getSize());  // Taille de la page

        return inventairePagineDto;
    }

    public List<Inventaire> getInventaireByYear(int year) {
        final LocalDate now = LocalDate.now();
        final int currentYear = now.getYear();
        if (year > 0) {
            return inventaireRepository.findByInventaireByYear(year);
        }
        return inventaireRepository.findByInventaireByYear(currentYear);
    }

    private InventaireDto getInventaireDto(Inventaire inventaire) {
        InventaireDto inventaireDto = new InventaireDto();
        inventaireDto.setId(inventaire.getId());
        inventaireDto.setArrivage(inventaire.getArrivage());
        inventaireDto.setMagasin(inventaire.getMagasin());
        inventaireDto.setDateInventaire(inventaire.getDateInventaire());
        inventaireDto.setCommentaire(inventaire.getCommentaire());
        inventaireDto.setBailleur(inventaire.getBailleur());
        inventaireDto.setProduit(inventaire.getProduit());
        inventaireDto.setPromo(inventaire.getPromo());
        inventaireDto.setVente(inventaire.getVente());
        inventaireDto.setPerteAbime(inventaire.getPerteAbime());
        inventaireDto.setStockAgent(inventaire.getStockAgent());
        inventaireDto.setStockMagasinCentral(inventaire.getStockMagasinCentral());
        inventaireDto.setStockAntenne(inventaire.getStockAntenne());
        inventaireDto.setStockTheoDebut(inventaire.getStockTheoDebut());
        inventaireDto.setStockReel(inventaire.getStockReel());
        inventaireDto.setStockFinalTheorique(inventaire.getStockFinalTheorique());
        return inventaireDto;
    }


    public InventaireDto getEtatProduit(String nomProduit, String dateInventaire) {
        InventaireDto inventaire = new InventaireDto();

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
            inventaire.setProduit(etatGlobal.getProduit());
            inventaire.setStockFinalTheorique(etatGlobal.getQuantiteTotalEnStock());
            inventaire.setStockTheoDebut(etatGlobal.getQuantiteTotalEnStock());
            inventaire.setArrivage(etatGlobal.getArrivage());
            inventaire.setVente(etatGlobal.getQuantiteVendue());
            inventaire.setPromo(etatGlobal.getQuantitePromotion());
            inventaire.setPerteAbime(etatGlobal.getQuantitePerte());

        }

        return inventaire;
    }

}
