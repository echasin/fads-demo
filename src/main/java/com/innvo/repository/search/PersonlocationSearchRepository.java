package com.innvo.repository.search;

import com.innvo.domain.Personlocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Personlocation entity.
 */
public interface PersonlocationSearchRepository extends ElasticsearchRepository<Personlocation, Long> {
}
