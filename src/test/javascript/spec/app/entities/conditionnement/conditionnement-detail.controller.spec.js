'use strict';

describe('Controller Tests', function() {

    describe('Conditionnement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockConditionnement, MockProduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockConditionnement = jasmine.createSpy('MockConditionnement');
            MockProduit = jasmine.createSpy('MockProduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Conditionnement': MockConditionnement,
                'Produit': MockProduit
            };
            createController = function() {
                $injector.get('$controller')("ConditionnementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:conditionnementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
