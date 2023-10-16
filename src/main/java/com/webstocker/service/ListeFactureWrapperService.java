package com.webstocker.service;

import com.webstocker.domain.wrapper.ListeFactureWrapper;
import java.util.List;

/**
 *
 * @author Athanase * Service Interface for managing ListeFactureWrapper.
 */
public interface ListeFactureWrapperService {

    List<ListeFactureWrapper> trouverFactureAunePeriode(String dateDebut, String dateFin);

    List<ListeFactureWrapper> trouverFactureParNumeroFacture(String numeroFacture);

    List<ListeFactureWrapper> creanceClients(String dateCreance);

    List<ListeFactureWrapper> trouverFactureZeroParPeriode(String dateDebut, String dateFin);

    List<ListeFactureWrapper> trouverFactureZeroParNumeroFacture(String numeroFacture);
}
