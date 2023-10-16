'use strict';

describe('Controller Tests', function() {

    describe('Localite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLocalite, MockMagasin, MockClient, MockMission, MockCommune;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLocalite = jasmine.createSpy('MockLocalite');
            MockMagasin = jasmine.createSpy('MockMagasin');
            MockClient = jasmine.createSpy('MockClient');
            MockMission = jasmine.createSpy('MockMission');
            MockCommune = jasmine.createSpy('MockCommune');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Localite': MockLocalite,
                'Magasin': MockMagasin,
                'Client': MockClient,
                'Mission': MockMission,
                'Commune': MockCommune
            };
            createController = function() {
                $injector.get('$controller')("LocaliteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:localiteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
