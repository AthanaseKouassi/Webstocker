'use strict';

describe('Controller Tests', function() {

    describe('TypeActivite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTypeActivite, MockActivite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTypeActivite = jasmine.createSpy('MockTypeActivite');
            MockActivite = jasmine.createSpy('MockActivite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TypeActivite': MockTypeActivite,
                'Activite': MockActivite
            };
            createController = function() {
                $injector.get('$controller')("TypeActiviteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:typeActiviteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
