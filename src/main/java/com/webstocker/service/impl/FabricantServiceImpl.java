package com.webstocker.service.impl;

import com.webstocker.domain.Produit;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.FabricantService;
import com.webstocker.domain.Fabricant;
import com.webstocker.repository.FabricantRepository;
import com.webstocker.repository.search.FabricantSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Fabricant.
 */
@Service
@Transactional
public class FabricantServiceImpl implements FabricantService{

    private final Logger log = LoggerFactory.getLogger(FabricantServiceImpl.class);

    @Inject
    private FabricantRepository fabricantRepository;

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private FabricantSearchRepository fabricantSearchRepository;

    /**
     * Save a fabricant.
     *
     * @param fabricant the entity to save
     * @return the persisted entity
     */
    public Fabricant save(Fabricant fabricant) {
        log.debug("Request to save Fabricant : {}", fabricant);
        Fabricant result = fabricantRepository.save(fabricant);
        fabricantSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the fabricants.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Fabricant> findAll() {
        log.debug("Request to get all Fabricants");
        List<Fabricant> result = fabricantRepository.findAllWithEagerRelationships();
        return result;
    }

    /**
     *  Get one fabricant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Fabricant findOne(Long id) {
        log.debug("Request to get Fabricant : {}", id);
        Fabricant fabricant = fabricantRepository.findOneWithEagerRelationships(id);
        return fabricant;
    }

    /**
     *  Delete the  fabricant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Fabricant : {}", id);
        fabricantRepository.delete(id);
        fabricantSearchRepository.delete(id);
    }

    /**
     * Search for the fabricant corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Fabricant> search(String query) {
        log.debug("Request to search Fabricants for query {}", query);
        return StreamSupport
            .stream(fabricantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Fabricant> findAllByProduit(Long idProduit) {
        log.debug("Request to search Fabricants for produit id {}", idProduit);
        List<Fabricant> fabricants = this.findAll();

        Produit p=produitRepository.findOne(idProduit);

        Iterator<Fabricant> iterator = fabricants.iterator();

        while (iterator.hasNext()){
            Fabricant fabricant = iterator.next();
            Set<Produit> produits = fabricant.getProduits();
            Iterator<Produit> produitIterator = produits.iterator();
            boolean hasProduct=false;
            while (produitIterator.hasNext()){
                Produit produit = produitIterator.next();
                if(produit.equals(p)){
                    hasProduct=true;
                    break;
                }
            }
            if(!hasProduct){
                iterator.remove();
            }
        }


        return fabricants;
    }
}
