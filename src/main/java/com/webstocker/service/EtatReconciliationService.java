package com.webstocker.service;

import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface EtatReconciliationService {
    List<EtatDeReconciliationWrapper> etatGlobalReconciliation(String madate);
}
