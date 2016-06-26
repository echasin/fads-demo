(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('OrganizationlocationDeleteController',OrganizationlocationDeleteController);

    OrganizationlocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Organizationlocation'];

    function OrganizationlocationDeleteController($uibModalInstance, entity, Organizationlocation) {
        var vm = this;

        vm.organizationlocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Organizationlocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
