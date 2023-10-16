'use strict';

describe('Controller Tests', function() {

    describe('LigneBudget Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLigneBudget, MockBudget, MockMission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLigneBudget = jasmine.createSpy('MockLigneBudget');
            MockBudget = jasmine.createSpy('MockBudget');
            MockMission = jasmine.createSpy('MockMission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LigneBudget': MockLigneBudget,
                'Budget': MockBudget,
                'Mission': MockMission
            };
            createController = function() {
                $injector.get('$controller')("LigneBudgetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:ligneBudgetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
