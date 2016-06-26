'use strict';

describe('Controller Tests', function() {

    describe('Roleorganizationperson Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRoleorganizationperson, MockOrganization, MockPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRoleorganizationperson = jasmine.createSpy('MockRoleorganizationperson');
            MockOrganization = jasmine.createSpy('MockOrganization');
            MockPerson = jasmine.createSpy('MockPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Roleorganizationperson': MockRoleorganizationperson,
                'Organization': MockOrganization,
                'Person': MockPerson
            };
            createController = function() {
                $injector.get('$controller')("RoleorganizationpersonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:roleorganizationpersonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
