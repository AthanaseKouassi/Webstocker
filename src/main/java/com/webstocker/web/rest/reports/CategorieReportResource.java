package com.webstocker.web.rest.reports;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Categorie;
import com.webstocker.reports.CategoriesReport;
import com.webstocker.service.CategorieService;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * REST controller for managing Categorie.
 */
@RestController
@RequestMapping("/api")
public class CategorieReportResource {

    private final Logger log = LoggerFactory.getLogger(CategorieReportResource.class);
        
    @Inject
    private CategorieService categorieService;
       
    @RequestMapping("/report/categories/liste")
    public void listeCategorieReports(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/pdf");
        List<Categorie> all = categorieService.findAll();
        OutputStream out = null;
        try {
            CategoriesReport categoriesReport = new CategoriesReport(all);
            out = response.getOutputStream();
            categoriesReport.build().toPdf(out);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
