'use strict';

describe('Controller Tests', function() {

    describe('Person Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPerson, MockRoleorganizationperson, MockPersonlocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPerson = jasmine.createSpy('MockPerson');
            MockRoleorganizationperson = jasmine.createSpy('MockRoleorganizationperson');
            MockPersonlocation = jasmine.createSpy('MockPersonlocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Person': MockPerson,
                'Roleorganizationperson': MockRoleorganizationperson,
                'Personlocation': MockPersonlocation
            };
            createController = function() {
                $injector.get('$controller')("PersonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:personUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
