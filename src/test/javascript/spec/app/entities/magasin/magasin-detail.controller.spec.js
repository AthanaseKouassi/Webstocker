'use strict';

describe('Controller Tests', function() {

    describe('Magasin Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMagasin, MockLocalite, MockLivraison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMagasin = jasmine.createSpy('MockMagasin');
            MockLocalite = jasmine.createSpy('MockLocalite');
            MockLivraison = jasmine.createSpy('MockLivraison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Magasin': MockMagasin,
                'Localite': MockLocalite,
                'Livraison': MockLivraison
            };
            createController = function() {
                $injector.get('$controller')("MagasinDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:magasinUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
