'use strict';

describe('Controller Tests', function() {

    describe('Commande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCommande, MockLivraison, MockLignecommande, MockBailleur, MockFabricant, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCommande = jasmine.createSpy('MockCommande');
            MockLivraison = jasmine.createSpy('MockLivraison');
            MockLignecommande = jasmine.createSpy('MockLignecommande');
            MockBailleur = jasmine.createSpy('MockBailleur');
            MockFabricant = jasmine.createSpy('MockFabricant');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Commande': MockCommande,
                'Livraison': MockLivraison,
                'Lignecommande': MockLignecommande,
                'Bailleur': MockBailleur,
                'Fabricant': MockFabricant,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("CommandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:commandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
