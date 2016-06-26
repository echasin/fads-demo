(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('RoleorganizationpersonDialogController', RoleorganizationpersonDialogController);

    RoleorganizationpersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Roleorganizationperson', 'Organization', 'Person'];

    function RoleorganizationpersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Roleorganizationperson, Organization, Person) {
        var vm = this;

        vm.roleorganizationperson = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.organizations = Organization.query();
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.roleorganizationperson.id !== null) {
                Roleorganizationperson.update(vm.roleorganizationperson, onSaveSuccess, onSaveError);
            } else {
                Roleorganizationperson.save(vm.roleorganizationperson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fadsiiApp:roleorganizationpersonUpdate', result);
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
