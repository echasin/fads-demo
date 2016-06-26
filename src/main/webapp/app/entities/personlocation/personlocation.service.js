(function() {
    'use strict';
    angular
        .module('fadsiiApp')
        .factory('Personlocation', Personlocation);

    Personlocation.$inject = ['$resource', 'DateUtils'];

    function Personlocation ($resource, DateUtils) {
        var resourceUrl =  'api/personlocations/:id';

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
