'use strict';

describe('Controller Tests', function() {

    describe('Lignefacture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLignefacture, MockLot, MockFacture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLignefacture = jasmine.createSpy('MockLignefacture');
            MockLot = jasmine.createSpy('MockLot');
            MockFacture = jasmine.createSpy('MockFacture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lignefacture': MockLignefacture,
                'Lot': MockLot,
                'Facture': MockFacture
            };
            createController = function() {
                $injector.get('$controller')("LignefactureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:lignefactureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
