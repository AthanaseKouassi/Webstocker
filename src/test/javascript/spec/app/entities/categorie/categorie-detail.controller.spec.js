'use strict';

describe('Controller Tests', function() {

    describe('Categorie Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategorie, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategorie = jasmine.createSpy('MockCategorie');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Categorie': MockCategorie,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("CategorieDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:categorieUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
