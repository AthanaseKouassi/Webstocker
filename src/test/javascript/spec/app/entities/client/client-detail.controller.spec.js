'use strict';

describe('Controller Tests', function() {

    describe('Client Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClient, MockFacture, MockLocalite, MockCategorieclient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClient = jasmine.createSpy('MockClient');
            MockFacture = jasmine.createSpy('MockFacture');
            MockLocalite = jasmine.createSpy('MockLocalite');
            MockCategorieclient = jasmine.createSpy('MockCategorieclient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Client': MockClient,
                'Facture': MockFacture,
                'Localite': MockLocalite,
                'Categorieclient': MockCategorieclient
            };
            createController = function() {
                $injector.get('$controller')("ClientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:clientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
