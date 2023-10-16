'use strict';

describe('Controller Tests', function() {

    describe('Reglement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReglement, MockFacture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReglement = jasmine.createSpy('MockReglement');
            MockFacture = jasmine.createSpy('MockFacture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Reglement': MockReglement,
                'Facture': MockFacture
            };
            createController = function() {
                $injector.get('$controller')("ReglementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:reglementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
