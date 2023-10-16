(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('InventaireDetailController', InventaireDetailController);

    InventaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Inventaire', 'Produit', 'Magasin'];

    function InventaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Inventaire, Produit, Magasin) {
        var vm = this;

        vm.inventaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webstockerApp:inventaireUpdate', function(event, result) {
            vm.inventaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
