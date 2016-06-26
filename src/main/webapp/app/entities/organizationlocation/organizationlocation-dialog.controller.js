(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .controller('OrganizationlocationDialogController', OrganizationlocationDialogController);

    OrganizationlocationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Organizationlocation', 'Organization', 'Location'];

    function OrganizationlocationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Organizationlocation, Organization, Location) {
        var vm = this;

        vm.organizationlocation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.organizations = Organization.query();
        vm.locations = Location.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.organizationlocation.id !== null) {
                Organizationlocation.update(vm.organizationlocation, onSaveSuccess, onSaveError);
            } else {
                Organizationlocation.save(vm.organizationlocation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('fadsiiApp:organizationlocationUpdate', result);
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
