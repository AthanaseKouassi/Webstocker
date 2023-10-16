'use strict';

describe('Controller Tests', function() {

    describe('Fabricant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFabricant, MockCommande, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFabricant = jasmine.createSpy('MockFabricant');
            MockCommande = jasmine.createSpy('MockCommande');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fabricant': MockFabricant,
                'Commande': MockCommande,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("FabricantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:fabricantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
