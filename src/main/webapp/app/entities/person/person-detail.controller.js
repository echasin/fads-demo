(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person', 'Roleorganizationperson', 'Personlocation'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person, Roleorganizationperson, Personlocation) {
        var vm = this;

        vm.person = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
