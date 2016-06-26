(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('PersonlocationDetailController', PersonlocationDetailController);

    PersonlocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Personlocation', 'Person', 'Location'];

    function PersonlocationDetailController($scope, $rootScope, $stateParams, entity, Personlocation, Person, Location) {
        var vm = this;

        vm.personlocation = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:personlocationUpdate', function(event, result) {
            vm.personlocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
