'use strict';

describe('Controller Tests', function() {

    describe('Categorieclient Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategorieclient, MockClient, MockPrix;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategorieclient = jasmine.createSpy('MockCategorieclient');
            MockClient = jasmine.createSpy('MockClient');
            MockPrix = jasmine.createSpy('MockPrix');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Categorieclient': MockCategorieclient,
                'Client': MockClient,
                'Prix': MockPrix
            };
            createController = function() {
                $injector.get('$controller')("CategorieclientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'webstockerApp:categorieclientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
