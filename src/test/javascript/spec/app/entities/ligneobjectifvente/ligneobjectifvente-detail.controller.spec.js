'use strict';

describe('Controller Tests', function() {

    describe('Ligneobjectifvente Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLigneobjectifvente, MockObjectifs, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLigneobjectifvente = jasmine.createSpy('MockLigneobjectifvente');
            MockObjectifs = jasmine.createSpy('MockObjectifs');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ligneobjectifvente': MockLigneobjectifvente,
                'Objectifs': MockObjectifs,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("LigneobjectifventeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:ligneobjectifventeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
