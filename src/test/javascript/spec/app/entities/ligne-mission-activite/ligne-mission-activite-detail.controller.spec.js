'use strict';

describe('Controller Tests', function() {

    describe('LigneMissionActivite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLigneMissionActivite, MockActivite, MockMission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLigneMissionActivite = jasmine.createSpy('MockLigneMissionActivite');
            MockActivite = jasmine.createSpy('MockActivite');
            MockMission = jasmine.createSpy('MockMission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LigneMissionActivite': MockLigneMissionActivite,
                'Activite': MockActivite,
                'Mission': MockMission
            };
            createController = function() {
                $injector.get('$controller')("LigneMissionActiviteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:ligneMissionActiviteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
