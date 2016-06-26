(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .factory('OrganizationlocationSearch', OrganizationlocationSearch);

    OrganizationlocationSearch.$inject = ['$resource'];

    function OrganizationlocationSearch($resource) {
        var resourceUrl =  'api/_search/organizationlocations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
