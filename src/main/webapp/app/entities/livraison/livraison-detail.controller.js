(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LivraisonDetailController', LivraisonDetailController);

    LivraisonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Livraison', 'Magasin', 'Commande', 'Lignelivraison'];

    function LivraisonDetailController($scope, $rootScope, $stateParams, entity, Livraison, Magasin, Commande, Lignelivraison) {
        var vm = this;
        vm.livraison = entity;
        
        var unsubscribe = $rootScope.$on('webstockerApp:livraisonUpdate', function(event, result) {
            vm.livraison = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
