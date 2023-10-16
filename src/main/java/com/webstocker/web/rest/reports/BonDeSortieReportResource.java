package com.webstocker.web.rest.reports;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lot;
import com.webstocker.reports.BonDeSortiesReport;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.reports.ListeLotProduitSortieParMagasinsReport;
import com.webstocker.reports.ListeProduitsVendusMagasinReport;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.LotService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class BonDeSortieReportResource {

    private final Logger log = LoggerFactory.getLogger(BonDeSortieReportResource.class);

    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;

    @Inject
    private BonDeSortieService bonDeSortieService;

    @RequestMapping("/report/lignebondesorties/bondesortie")
    public void bondesortieproduit(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        response.setContentType("application/pdf");
        Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;

        try {
            BonDeSortiesReport bsReport = new BonDeSortiesReport(all.getContent());
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequestMapping(value = "/report/lignebondesorties/bondesortieperte/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] bondesortiePromotion(HttpServletRequest request,
            HttpServletResponse response, Pageable pageable, @PathVariable Long id) {
        response.setContentType("application/pdf");

        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesPerte(bonDeSortie);
//        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortiePromotion(bonDeSortie);
        //Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;

        try {
            BonDeSortiesReport bsReport = new BonDeSortiesReport(ligneBonDeSorties);
            out = response.getOutputStream();
            // response.setHeader("Content-Disposition", "attachment; filename=\"" + "test.pdf" + "\"");
            bsReport.build().toPdf(out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/report/lignebondesorties/quantitevenduparmagasin/{typeSortie}/{nomMagasin}/{dateDebutPeriode}/{dateFinPeriode}", method = RequestMethod.GET)
    public void listeProduitVenduParMagasin(HttpServletRequest request,
            HttpServletResponse response, Pageable pageable, @PathVariable String typeSortie, @PathVariable String nomMagasin,
            @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.venteRealiseParMagasin(typeSortie, nomMagasin, dateDebutPeriode, dateFinPeriode);
        // Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;

        try {
            ListeProduitsVendusMagasinReport bsReport = new ListeProduitsVendusMagasinReport(ligneBonDeSorties, dateDebutPeriode, dateFinPeriode);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Le Bordereau de livraison est remis lors de la sortie des produits d'un
     * magasin
     *
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping(value = "/report/lignelivraisons/bordereaulivraison/{id}", method = RequestMethod.GET)
    public void bordereauParLivraisonPromotionReports(HttpServletRequest request,
            HttpServletResponse response, @PathVariable Long id) {
        response.setContentType("application/pdf");

        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.RecupererLigneBonDesortiePourBordereauLivraison(bonDeSortie);
        OutputStream out = null;
        try {
            BordereauLivraisonsReport blReport = new BordereauLivraisonsReport(ligneBonDeSorties);
            out = response.getOutputStream();
            blReport.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/report/lignebondesorties/quantitelotproduitvenduparmagasin/{typeSortie}/{nomMagasin}/{dateDebutPeriode}/{dateFinPeriode}/{produit}", method = RequestMethod.GET)
    public void listeLotsProduitsVendusParMagasin(HttpServletRequest request,
            HttpServletResponse response, Pageable pageable, @PathVariable String typeSortie, @PathVariable String nomMagasin,
            @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode, @PathVariable String produit) {

        response.setContentType("application/pdf");

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.sortieRealiseParMagasin(typeSortie, nomMagasin, dateDebutPeriode, dateFinPeriode, produit);

        OutputStream out = null;

        try {
            ListeLotProduitSortieParMagasinsReport bsReport = new ListeLotProduitSortieParMagasinsReport(ligneBonDeSorties, dateDebutPeriode, dateFinPeriode);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/report/bondesorties/toutbondesortie/{id}", method = RequestMethod.GET)
    public void toutBonDeSortie(HttpServletRequest request,
            HttpServletResponse response, Pageable pageable, @PathVariable Long id) {
        response.setContentType("application/pdf");
        String typesortie;
        List<LigneBonDeSortie> ligneBonDeSorties;
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        typesortie = bonDeSortie.getTypeSortie().toString();
        switch (typesortie) {
            case "PERTE":
                ligneBonDeSorties = ligneBonDeSortieService.recupererLignesPerte(bonDeSortie);
//                ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortiePromotion(bonDeSortie);
                System.err.println("BON DE SORTIE p POUR "+typesortie);
                break;
            case "PROMOTION":
                ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortiePromotion(bonDeSortie);
                System.err.println("BON DE SORTIE pr POUR "+typesortie);
                break;
            case "TRANSFERT":
                ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortieTransfert(bonDeSortie);
                System.err.println("BON DE SORTIE t POUR "+typesortie);
                break;
            default:
                ligneBonDeSorties = ligneBonDeSortieService.recupererLignesFacture(bonDeSortie);
                System.err.println("BON DE SORTIE v POUR "+typesortie);
                break;
        }

        // List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortieTransfert(bonDeSortie);
        //Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;

        try {
            BonDeSortiesReport bsReport = new BonDeSortiesReport(ligneBonDeSorties);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
