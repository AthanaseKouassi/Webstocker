'use strict';

describe('Controller Tests', function() {

    describe('Cellule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCellule, MockUser, MockMission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCellule = jasmine.createSpy('MockCellule');
            MockUser = jasmine.createSpy('MockUser');
            MockMission = jasmine.createSpy('MockMission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Cellule': MockCellule,
                'User': MockUser,
                'Mission': MockMission
            };
            createController = function() {
                $injector.get('$controller')("CelluleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:celluleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
