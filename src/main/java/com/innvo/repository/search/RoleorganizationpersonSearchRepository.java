package com.innvo.repository.search;

import com.innvo.domain.Roleorganizationperson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Roleorganizationperson entity.
 */
public interface RoleorganizationpersonSearchRepository extends ElasticsearchRepository<Roleorganizationperson, Long> {
}
