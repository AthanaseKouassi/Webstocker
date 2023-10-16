'use strict';

describe('Controller Tests', function() {

    describe('LigneBonDeSortie Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLigneBonDeSortie, MockProduit, MockBonDeSortie, MockLot;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLigneBonDeSortie = jasmine.createSpy('MockLigneBonDeSortie');
            MockProduit = jasmine.createSpy('MockProduit');
            MockBonDeSortie = jasmine.createSpy('MockBonDeSortie');
            MockLot = jasmine.createSpy('MockLot');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LigneBonDeSortie': MockLigneBonDeSortie,
                'Produit': MockProduit,
                'BonDeSortie': MockBonDeSortie,
                'Lot': MockLot
            };
            createController = function() {
                $injector.get('$controller')("LigneBonDeSortieDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:ligneBonDeSortieUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
