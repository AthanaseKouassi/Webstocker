'use strict';

describe('Controller Tests', function() {

    describe('Commune Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCommune, MockVille;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCommune = jasmine.createSpy('MockCommune');
            MockVille = jasmine.createSpy('MockVille');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Commune': MockCommune,
                'Ville': MockVille
            };
            createController = function() {
                $injector.get('$controller')("CommuneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:communeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
