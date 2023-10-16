'use strict';

describe('Controller Tests', function() {

    describe('Lignecommande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLignecommande, MockCommande, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLignecommande = jasmine.createSpy('MockLignecommande');
            MockCommande = jasmine.createSpy('MockCommande');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lignecommande': MockLignecommande,
                'Commande': MockCommande,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("LignecommandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:lignecommandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
