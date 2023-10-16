'use strict';

describe('Controller Tests', function() {

    describe('Lignelivraison Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLignelivraison, MockLot, MockLivraison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLignelivraison = jasmine.createSpy('MockLignelivraison');
            MockLot = jasmine.createSpy('MockLot');
            MockLivraison = jasmine.createSpy('MockLivraison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lignelivraison': MockLignelivraison,
                'Lot': MockLot,
                'Livraison': MockLivraison
            };
            createController = function() {
                $injector.get('$controller')("LignelivraisonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:lignelivraisonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
