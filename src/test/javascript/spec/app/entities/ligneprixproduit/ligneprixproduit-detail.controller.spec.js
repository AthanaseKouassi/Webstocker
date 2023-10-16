'use strict';

describe('Controller Tests', function() {

    describe('Ligneprixproduit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLigneprixproduit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLigneprixproduit = jasmine.createSpy('MockLigneprixproduit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ligneprixproduit': MockLigneprixproduit
            };
            createController = function() {
                $injector.get('$controller')("LigneprixproduitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:ligneprixproduitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
