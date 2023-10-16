(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignecommandeDetailController', LignecommandeDetailController);

    LignecommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lignecommande', 'Commande', 'Produit'];

    function LignecommandeDetailController($scope, $rootScope, $stateParams, entity, Lignecommande, Commande, Produit) {
        var vm = this;
        vm.lignecommande = entity;
        vm.load = function (id) {
            Lignecommande.get({id: id}, function(result) {
                vm.lignecommande = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:lignecommandeUpdate', function(event, result) {
            vm.lignecommande = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
