'use strict';

describe('Controller Tests', function() {

    describe('Inventaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInventaire, MockProduit, MockMagasin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInventaire = jasmine.createSpy('MockInventaire');
            MockProduit = jasmine.createSpy('MockProduit');
            MockMagasin = jasmine.createSpy('MockMagasin');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Inventaire': MockInventaire,
                'Produit': MockProduit,
                'Magasin': MockMagasin
            };
            createController = function() {
                $injector.get('$controller')("InventaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:inventaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
