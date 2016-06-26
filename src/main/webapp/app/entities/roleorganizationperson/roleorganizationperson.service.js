(function() {
    'use strict';
    angular
        .module('fadsiiApp')
        .factory('Roleorganizationperson', Roleorganizationperson);

    Roleorganizationperson.$inject = ['$resource', 'DateUtils'];

    function Roleorganizationperson ($resource, DateUtils) {
        var resourceUrl =  'api/roleorganizationpeople/:id';

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
