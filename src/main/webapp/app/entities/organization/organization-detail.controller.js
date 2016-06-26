(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('OrganizationDetailController', OrganizationDetailController);

    OrganizationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Organization', 'Roleorganizationperson', 'Organizationlocation'];

    function OrganizationDetailController($scope, $rootScope, $stateParams, entity, Organization, Roleorganizationperson, Organizationlocation) {
        var vm = this;

        vm.organization = entity;

        var unsubscribe = $rootScope.$on('fadsiiApp:organizationUpdate', function(event, result) {
            vm.organization = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
