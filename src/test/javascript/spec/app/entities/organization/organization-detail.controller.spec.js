'use strict';

describe('Controller Tests', function() {

    describe('Organization Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrganization, MockRoleorganizationperson, MockOrganizationlocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrganization = jasmine.createSpy('MockOrganization');
            MockRoleorganizationperson = jasmine.createSpy('MockRoleorganizationperson');
            MockOrganizationlocation = jasmine.createSpy('MockOrganizationlocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Organization': MockOrganization,
                'Roleorganizationperson': MockRoleorganizationperson,
                'Organizationlocation': MockOrganizationlocation
            };
            createController = function() {
                $injector.get('$controller')("OrganizationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:organizationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
