(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('OrganizationlocationDetailController', OrganizationlocationDetailController);

    OrganizationlocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Organizationlocation', 'Organization', 'Location'];

    function OrganizationlocationDetailController($scope, $rootScope, $stateParams, entity, Organizationlocation, Organization, Location) {
        var vm = this;

        vm.organizationlocation = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:organizationlocationUpdate', function(event, result) {
            vm.organizationlocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
