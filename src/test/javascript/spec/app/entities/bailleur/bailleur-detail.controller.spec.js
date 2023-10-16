'use strict';

describe('Controller Tests', function() {

    describe('Bailleur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBailleur, MockCommande;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBailleur = jasmine.createSpy('MockBailleur');
            MockCommande = jasmine.createSpy('MockCommande');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Bailleur': MockBailleur,
                'Commande': MockCommande
            };
            createController = function() {
                $injector.get('$controller')("BailleurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:bailleurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
