(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommandeDetailController', CommandeDetailController);

    CommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Commande', 'Livraison', 'Lignecommande', 'Bailleur', 'Fabricant', 'Produit'];

    function CommandeDetailController($scope, $rootScope, $stateParams, entity, Commande, Livraison, Lignecommande, Bailleur, Fabricant, Produit) {
        var vm = this;
        vm.commande = entity;
        vm.load = function (id) {
            Commande.get({id: id}, function(result) {
                vm.commande = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:commandeUpdate', function(event, result) {
            vm.commande = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
