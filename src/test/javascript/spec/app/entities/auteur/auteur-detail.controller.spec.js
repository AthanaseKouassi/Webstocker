'use strict';

describe('Controller Tests', function() {

    describe('Auteur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAuteur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAuteur = jasmine.createSpy('MockAuteur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Auteur': MockAuteur
            };
            createController = function() {
                $injector.get('$controller')("AuteurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:auteurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
