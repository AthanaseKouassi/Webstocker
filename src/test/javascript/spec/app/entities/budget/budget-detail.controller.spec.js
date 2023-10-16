'use strict';

describe('Controller Tests', function() {

    describe('Budget Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBudget, MockLigneBudget;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBudget = jasmine.createSpy('MockBudget');
            MockLigneBudget = jasmine.createSpy('MockLigneBudget');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Budget': MockBudget,
                'LigneBudget': MockLigneBudget
            };
            createController = function() {
                $injector.get('$controller')("BudgetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:budgetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
