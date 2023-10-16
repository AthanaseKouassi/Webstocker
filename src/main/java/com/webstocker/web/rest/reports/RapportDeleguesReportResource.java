package com.webstocker.web.rest.reports;

import com.webstocker.domain.wrapper.EtatAgentWrapper;
import com.webstocker.reports.RapportDesDeleguesReport;
import com.webstocker.service.EtatAgentWrapperService;
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
public class RapportDeleguesReportResource {

    private final Logger log = LoggerFactory.getLogger(RapportDeleguesReportResource.class);

    @Inject
    EtatAgentWrapperService etatAgentWrapperService;

    @RequestMapping(value = "/report/rapportdelegue/{nomDelegue}/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public void rapportDesDelegues(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String nomDelegue, @PathVariable String dateDebut, @PathVariable String dateFin) {

        response.setContentType("application/pdf");

        List<EtatAgentWrapper> all = etatAgentWrapperService.RapportAgentParPeriode(nomDelegue, dateDebut, dateFin);

        OutputStream out = null;

        try {
            RapportDesDeleguesReport rapport = new RapportDesDeleguesReport(all, dateDebut, dateFin);
            out = response.getOutputStream();
            rapport.build().toPdf(out);
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
