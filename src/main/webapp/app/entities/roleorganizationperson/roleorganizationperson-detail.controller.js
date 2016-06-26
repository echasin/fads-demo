(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('RoleorganizationpersonDetailController', RoleorganizationpersonDetailController);

    RoleorganizationpersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Roleorganizationperson', 'Organization', 'Person'];

    function RoleorganizationpersonDetailController($scope, $rootScope, $stateParams, entity, Roleorganizationperson, Organization, Person) {
        var vm = this;

        vm.roleorganizationperson = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:roleorganizationpersonUpdate', function(event, result) {
            vm.roleorganizationperson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
