package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Produit;
import com.webstocker.exception.IllegalArgumentException;
import com.webstocker.exception.InvalideDateFormatException;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.utilitaires.Constantes;
import com.webstocker.utilitaires.Utils;
import lombok.extern.slf4j.Slf4j;
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
        if (year <= 0) {
            throw new IllegalArgumentException("L'année ne peut être inférieure ou égale zéro ");
        }
        if (idProduit == null) {
            throw new IllegalArgumentException("Id produit ne peut être null");
        }
        final Produit produit = produitRepository.findOne(idProduit);

        return inventaireRepository.findByInventaireByYearAndProduit(year, produit);
    }
}
