'use strict';

describe('Controller Tests', function() {

    describe('Organizationlocation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrganizationlocation, MockOrganization, MockLocation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrganizationlocation = jasmine.createSpy('MockOrganizationlocation');
            MockOrganization = jasmine.createSpy('MockOrganization');
            MockLocation = jasmine.createSpy('MockLocation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Organizationlocation': MockOrganizationlocation,
                'Organization': MockOrganization,
                'Location': MockLocation
            };
            createController = function() {
                $injector.get('$controller')("OrganizationlocationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fadsiiApp:organizationlocationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
