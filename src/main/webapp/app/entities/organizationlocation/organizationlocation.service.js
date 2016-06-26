(function() {
    'use strict';
    angular
        .module('fadsiiApp')
        .factory('Organizationlocation', Organizationlocation);

    Organizationlocation.$inject = ['$resource', 'DateUtils'];

    function Organizationlocation ($resource, DateUtils) {
        var resourceUrl =  'api/organizationlocations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastmodifieddatetime = DateUtils.convertDateTimeFromServer(data.lastmodifieddatetime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
