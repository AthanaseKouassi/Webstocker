(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BonDeSortieDetailController', BonDeSortieDetailController);

    BonDeSortieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BonDeSortie', 'Magasin', 'User', 'Client'];

    function BonDeSortieDetailController($scope, $rootScope, $stateParams, previousState, entity, BonDeSortie, Magasin, User, Client) {
        var vm = this;

        vm.bonDeSortie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webstockerApp:bonDeSortieUpdate', function(event, result) {
            vm.bonDeSortie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
