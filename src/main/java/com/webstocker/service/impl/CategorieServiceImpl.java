package com.webstocker.service.impl;

//import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import com.webstocker.service.CategorieService;
import com.webstocker.domain.Categorie;
import com.webstocker.repository.CategorieRepository;
import com.webstocker.repository.search.CategorieSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Categorie.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    @Inject
    private CategorieRepository categorieRepository;

    @Inject
    private CategorieSearchRepository categorieSearchRepository;

    /**
     * Save a categorie.
     *
     * @param categorie the entity to save
     * @return the persisted entity
     */
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        Categorie result = categorieRepository.save(categorie);
        categorieSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Categorie> findAll() {
        log.debug("Request to get all Categories");
        List<Categorie> result = categorieRepository.findAll();
        return result;
    }

    /**
     * Get one categorie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Categorie findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        Categorie categorie = categorieRepository.findOne(id);
        return categorie;
    }

    /**
     * Delete the categorie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.delete(id);
        categorieSearchRepository.delete(id);
    }

    /**
     * Search for the categorie corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Categorie> search(String query) {
        log.debug("Request to search Categories for query {}", query);
        return StreamSupport
                .stream(categorieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Categorie findByNomCategorie(String nomCategorie) {
        Categorie result = null;
        String nomcategorie = null;
        nomcategorie = nomCategorie;
        return result = categorieRepository.findByNomCategorie(nomcategorie);
    }
}
