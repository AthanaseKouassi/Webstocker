'use strict';

describe('Controller Tests', function() {

    describe('Mission Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMission, MockLigneMissionActivite, MockLigneBudget, MockLocalite, MockCellule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMission = jasmine.createSpy('MockMission');
            MockLigneMissionActivite = jasmine.createSpy('MockLigneMissionActivite');
            MockLigneBudget = jasmine.createSpy('MockLigneBudget');
            MockLocalite = jasmine.createSpy('MockLocalite');
            MockCellule = jasmine.createSpy('MockCellule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Mission': MockMission,
                'LigneMissionActivite': MockLigneMissionActivite,
                'LigneBudget': MockLigneBudget,
                'Localite': MockLocalite,
                'Cellule': MockCellule
            };
            createController = function() {
                $injector.get('$controller')("MissionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:missionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
