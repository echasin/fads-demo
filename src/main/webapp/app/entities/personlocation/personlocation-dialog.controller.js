(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('PersonlocationDialogController', PersonlocationDialogController);

    PersonlocationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Personlocation', 'Person', 'Location'];

    function PersonlocationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Personlocation, Person, Location) {
        var vm = this;

        vm.personlocation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.people = Person.query();
        vm.locations = Location.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personlocation.id !== null) {
                Personlocation.update(vm.personlocation, onSaveSuccess, onSaveError);
            } else {
                Personlocation.save(vm.personlocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fadsiiApp:personlocationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastmodifieddatetime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
