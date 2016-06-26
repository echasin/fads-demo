(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .factory('PersonlocationSearch', PersonlocationSearch);

    PersonlocationSearch.$inject = ['$resource'];

    function PersonlocationSearch($resource) {
        var resourceUrl =  'api/_search/personlocations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
