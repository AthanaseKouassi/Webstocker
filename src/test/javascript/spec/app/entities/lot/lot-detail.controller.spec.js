'use strict';

describe('Controller Tests', function() {

    describe('Lot Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLot, MockProduit, MockLignelivraison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLot = jasmine.createSpy('MockLot');
            MockProduit = jasmine.createSpy('MockProduit');
            MockLignelivraison = jasmine.createSpy('MockLignelivraison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lot': MockLot,
                'Produit': MockProduit,
                'Lignelivraison': MockLignelivraison
            };
            createController = function() {
                $injector.get('$controller')("LotDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:lotUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
