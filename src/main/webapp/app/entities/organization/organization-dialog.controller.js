(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('OrganizationDialogController', OrganizationDialogController);

    OrganizationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organization', 'Roleorganizationperson', 'Organizationlocation'];

    function OrganizationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Organization, Roleorganizationperson, Organizationlocation) {
        var vm = this;

        vm.organization = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.roleorganizationpeople = Roleorganizationperson.query();
        vm.organizationlocations = Organizationlocation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.organization.id !== null) {
                Organization.update(vm.organization, onSaveSuccess, onSaveError);
            } else {
                Organization.save(vm.organization, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fadsiiApp:organizationUpdate', result);
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
