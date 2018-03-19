package sk.beno.jhipstertest.repository.search;

import sk.beno.jhipstertest.domain.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Car entity.
 */
public interface CarSearchRepository extends ElasticsearchRepository<Car, Long> {
}
