(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('RoleorganizationpersonDeleteController',RoleorganizationpersonDeleteController);

    RoleorganizationpersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Roleorganizationperson'];

    function RoleorganizationpersonDeleteController($uibModalInstance, entity, Roleorganizationperson) {
        var vm = this;

        vm.roleorganizationperson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Roleorganizationperson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
