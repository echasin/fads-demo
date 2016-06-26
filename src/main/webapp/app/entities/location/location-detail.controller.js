(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Location', 'Organizationlocation', 'Personlocation'];

    function LocationDetailController($scope, $rootScope, $stateParams, entity, Location, Organizationlocation, Personlocation) {
        var vm = this;

        vm.location = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
