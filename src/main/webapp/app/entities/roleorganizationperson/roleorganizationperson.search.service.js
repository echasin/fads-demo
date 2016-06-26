(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .factory('RoleorganizationpersonSearch', RoleorganizationpersonSearch);

    RoleorganizationpersonSearch.$inject = ['$resource'];

    function RoleorganizationpersonSearch($resource) {
        var resourceUrl =  'api/_search/roleorganizationpeople/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
