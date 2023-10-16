package com.webstocker.service;

import com.webstocker.domain.wrapper.EtatAgentWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface EtatAgentWrapperService {
    List<EtatAgentWrapper> RapportAgentParPeriode(String nomUser, String dateDebut, String dateFin);
}
