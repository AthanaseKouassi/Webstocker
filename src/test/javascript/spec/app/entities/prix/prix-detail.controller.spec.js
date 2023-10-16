'use strict';

describe('Controller Tests', function() {

    describe('Prix Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPrix, MockProduit, MockCategorieclient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPrix = jasmine.createSpy('MockPrix');
            MockProduit = jasmine.createSpy('MockProduit');
            MockCategorieclient = jasmine.createSpy('MockCategorieclient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Prix': MockPrix,
                'Produit': MockProduit,
                'Categorieclient': MockCategorieclient
            };
            createController = function() {
                $injector.get('$controller')("PrixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:prixUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
