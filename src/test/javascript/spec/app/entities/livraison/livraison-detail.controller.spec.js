'use strict';

describe('Controller Tests', function() {

    describe('Livraison Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLivraison, MockMagasin, MockCommande, MockLignelivraison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLivraison = jasmine.createSpy('MockLivraison');
            MockMagasin = jasmine.createSpy('MockMagasin');
            MockCommande = jasmine.createSpy('MockCommande');
            MockLignelivraison = jasmine.createSpy('MockLignelivraison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Livraison': MockLivraison,
                'Magasin': MockMagasin,
                'Commande': MockCommande,
                'Lignelivraison': MockLignelivraison
            };
            createController = function() {
                $injector.get('$controller')("LivraisonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:livraisonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
