(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('PersonlocationDeleteController',PersonlocationDeleteController);

    PersonlocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Personlocation'];

    function PersonlocationDeleteController($uibModalInstance, entity, Personlocation) {
        var vm = this;

        vm.personlocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Personlocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
