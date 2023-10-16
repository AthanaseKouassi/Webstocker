(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ClientDetailController', ClientDetailController);

    ClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Client', 'Facture', 'Localite', 'Categorieclient'];

    function ClientDetailController($scope, $rootScope, $stateParams, previousState, entity, Client, Facture, Localite, Categorieclient) {
        var vm = this;

        vm.client = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webstockerApp:clientUpdate', function(event, result) {
            vm.client = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
