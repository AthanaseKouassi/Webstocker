(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('PrixDetailController', PrixDetailController);

    PrixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prix', 'Produit', 'Categorieclient'];

    function PrixDetailController($scope, $rootScope, $stateParams, previousState, entity, Prix, Produit, Categorieclient) {
        var vm = this;

        vm.prix = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webstockerApp:prixUpdate', function(event, result) {
            vm.prix = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
