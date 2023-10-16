'use strict';

describe('Controller Tests', function() {

    describe('Facture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFacture, MockClient, MockReglement, MockBonDeSortie;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFacture = jasmine.createSpy('MockFacture');
            MockClient = jasmine.createSpy('MockClient');
            MockReglement = jasmine.createSpy('MockReglement');
            MockBonDeSortie = jasmine.createSpy('MockBonDeSortie');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Facture': MockFacture,
                'Client': MockClient,
                'Reglement': MockReglement,
                'BonDeSortie': MockBonDeSortie
            };
            createController = function() {
                $injector.get('$controller')("FactureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:factureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
