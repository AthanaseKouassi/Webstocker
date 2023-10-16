'use strict';

describe('Controller Tests', function() {

    describe('Produit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduit, MockCategorie, MockObjectifs, MockLignecommande, MockConditionnement, MockLot, MockFabricant, MockPrix;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduit = jasmine.createSpy('MockProduit');
            MockCategorie = jasmine.createSpy('MockCategorie');
            MockObjectifs = jasmine.createSpy('MockObjectifs');
            MockLignecommande = jasmine.createSpy('MockLignecommande');
            MockConditionnement = jasmine.createSpy('MockConditionnement');
            MockLot = jasmine.createSpy('MockLot');
            MockFabricant = jasmine.createSpy('MockFabricant');
            MockPrix = jasmine.createSpy('MockPrix');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Produit': MockProduit,
                'Categorie': MockCategorie,
                'Objectifs': MockObjectifs,
                'Lignecommande': MockLignecommande,
                'Conditionnement': MockConditionnement,
                'Lot': MockLot,
                'Fabricant': MockFabricant,
                'Prix': MockPrix
            };
            createController = function() {
                $injector.get('$controller')("ProduitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:produitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
