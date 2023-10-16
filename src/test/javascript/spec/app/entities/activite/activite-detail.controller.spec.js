'use strict';

describe('Controller Tests', function() {

    describe('Activite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActivite, MockLigneMissionActivite, MockTypeActivite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActivite = jasmine.createSpy('MockActivite');
            MockLigneMissionActivite = jasmine.createSpy('MockLigneMissionActivite');
            MockTypeActivite = jasmine.createSpy('MockTypeActivite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Activite': MockActivite,
                'LigneMissionActivite': MockLigneMissionActivite,
                'TypeActivite': MockTypeActivite
            };
            createController = function() {
                $injector.get('$controller')("ActiviteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:activiteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
