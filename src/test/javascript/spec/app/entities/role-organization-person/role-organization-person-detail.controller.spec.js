'use strict';

describe('Controller Tests', function() {

    describe('Role_organization_person Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRole_organization_person, MockOrganization, MockPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRole_organization_person = jasmine.createSpy('MockRole_organization_person');
            MockOrganization = jasmine.createSpy('MockOrganization');
            MockPerson = jasmine.createSpy('MockPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Role_organization_person': MockRole_organization_person,
                'Organization': MockOrganization,
                'Person': MockPerson
            };
            createController = function() {
                $injector.get('$controller')("Role_organization_personDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:role_organization_personUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
