package com.webstocker.web.rest.reports;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.reports.EtatReconciliationMensuelReport;
import com.webstocker.reports.EtatStockGlobalParBailleurReport;
import com.webstocker.reports.EtatStockGlobalReport;
import com.webstocker.service.BailleurService;
import com.webstocker.service.EtatStockBailleurService;
import com.webstocker.service.EtatStockGlobalService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class EtatStockGlobalReportResource {

    private final Logger log = LoggerFactory.getLogger(EtatStockGlobalReportResource.class);

    @Inject
    EtatStockGlobalService etatStockGlobalService;

    @Inject
    EtatStockBailleurService etatStockBailleurService;
    
    @Inject
    BailleurService bailleurService;

    @RequestMapping(value = "/report/etatstockglobal/etatstockglobalaimas/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public void etatStockGlobalAimas(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateDebut, @PathVariable String dateFin) {
        response.setContentType("application/pdf");

        List<EtatStockGlobalAimasWrapper> all = etatStockGlobalService.etatStockGlobal(dateDebut, dateFin);

        OutputStream out = null;

        try {
            EtatStockGlobalReport bsReport = new EtatStockGlobalReport(all, dateDebut, dateFin);
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

    @RequestMapping(value = "/report/etatstockglobal/etatstockglobalbailleur/{nomBailleur}/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public void etatStockGlobalBailleur(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String nomBailleur, @PathVariable String dateDebut, @PathVariable String dateFin) {
        response.setContentType("application/pdf");

        List<EtatStockGlobalAimasWrapper> all = etatStockBailleurService.etatStockParBailleur(nomBailleur, dateDebut, dateFin);

        OutputStream out = null;

        try {
            EtatStockGlobalParBailleurReport bsReport = new EtatStockGlobalParBailleurReport(all, dateDebut, dateFin);
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
