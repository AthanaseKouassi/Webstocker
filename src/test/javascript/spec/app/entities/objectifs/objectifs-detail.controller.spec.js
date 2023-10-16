'use strict';

describe('Controller Tests', function() {

    describe('Objectifs Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockObjectifs, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockObjectifs = jasmine.createSpy('MockObjectifs');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Objectifs': MockObjectifs,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("ObjectifsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:objectifsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
