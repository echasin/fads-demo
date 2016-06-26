'use strict';

describe('Controller Tests', function() {

    describe('Personlocation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPersonlocation, MockPerson, MockLocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPersonlocation = jasmine.createSpy('MockPersonlocation');
            MockPerson = jasmine.createSpy('MockPerson');
            MockLocation = jasmine.createSpy('MockLocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Personlocation': MockPersonlocation,
                'Person': MockPerson,
                'Location': MockLocation
            };
            createController = function() {
                $injector.get('$controller')("PersonlocationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:personlocationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
