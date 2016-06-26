package com.innvo.repository.search;

import com.innvo.domain.Organizationlocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organizationlocation entity.
 */
public interface OrganizationlocationSearchRepository extends ElasticsearchRepository<Organizationlocation, Long> {
}
