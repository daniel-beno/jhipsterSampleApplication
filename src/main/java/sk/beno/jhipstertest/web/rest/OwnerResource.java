package sk.beno.jhipstertest.web.rest;

import com.codahale.metrics.annotation.Timed;
import sk.beno.jhipstertest.domain.Owner;

import sk.beno.jhipstertest.repository.OwnerRepository;
import sk.beno.jhipstertest.repository.search.OwnerSearchRepository;
import sk.beno.jhipstertest.web.rest.errors.BadRequestAlertException;
import sk.beno.jhipstertest.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Owner.
 */
@RestController
@RequestMapping("/api")
public class OwnerResource {

    private final Logger log = LoggerFactory.getLogger(OwnerResource.class);

    private static final String ENTITY_NAME = "owner";

    private final OwnerRepository ownerRepository;

    private final OwnerSearchRepository ownerSearchRepository;

    public OwnerResource(OwnerRepository ownerRepository, OwnerSearchRepository ownerSearchRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerSearchRepository = ownerSearchRepository;
    }

    /**
     * POST  /owners : Create a new owner.
     *
     * @param owner the owner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new owner, or with status 400 (Bad Request) if the owner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/owners")
    @Timed
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to save Owner : {}", owner);
        if (owner.getId() != null) {
            throw new BadRequestAlertException("A new owner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Owner result = ownerRepository.save(owner);
        ownerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /owners : Updates an existing owner.
     *
     * @param owner the owner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated owner,
     * or with status 400 (Bad Request) if the owner is not valid,
     * or with status 500 (Internal Server Error) if the owner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/owners")
    @Timed
    public ResponseEntity<Owner> updateOwner(@RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to update Owner : {}", owner);
        if (owner.getId() == null) {
            return createOwner(owner);
        }
        Owner result = ownerRepository.save(owner);
        ownerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, owner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /owners : get all the owners.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of owners in body
     */
    @GetMapping("/owners")
    @Timed
    public List<Owner> getAllOwners() {
        log.debug("REST request to get all Owners");
        return ownerRepository.findAll();
        }

    /**
     * GET  /owners/:id : get the "id" owner.
     *
     * @param id the id of the owner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the owner, or with status 404 (Not Found)
     */
    @GetMapping("/owners/{id}")
    @Timed
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        Owner owner = ownerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(owner));
    }

    /**
     * DELETE  /owners/:id : delete the "id" owner.
     *
     * @param id the id of the owner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/owners/{id}")
    @Timed
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.debug("REST request to delete Owner : {}", id);
        ownerRepository.delete(id);
        ownerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/owners?query=:query : search for the owner corresponding
     * to the query.
     *
     * @param query the query of the owner search
     * @return the result of the search
     */
    @GetMapping("/_search/owners")
    @Timed
    public List<Owner> searchOwners(@RequestParam String query) {
        log.debug("REST request to search Owners for query {}", query);
        return StreamSupport
            .stream(ownerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
