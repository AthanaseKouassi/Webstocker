'use strict';

describe('Controller Tests', function() {

    describe('BonDeSortie Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBonDeSortie, MockMagasin, MockUser, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBonDeSortie = jasmine.createSpy('MockBonDeSortie');
            MockMagasin = jasmine.createSpy('MockMagasin');
            MockUser = jasmine.createSpy('MockUser');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BonDeSortie': MockBonDeSortie,
                'Magasin': MockMagasin,
                'User': MockUser,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("BonDeSortieDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:bonDeSortieUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
