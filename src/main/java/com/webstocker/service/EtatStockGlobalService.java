/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.service;

import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Athanase
 */
public interface EtatStockGlobalService {

    List<EtatStockGlobalAimasWrapper> etatStockGlobal(String dateDebut, String dateFin);

    List<EtatStockGlobalAimasWrapper> etatStockGlobalNew(LocalDate dateDebut, LocalDate dateFin);
}
