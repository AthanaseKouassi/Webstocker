(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignelivraisonDetailController', LignelivraisonDetailController);

    LignelivraisonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lignelivraison', 'Lot', 'Livraison'];

    function LignelivraisonDetailController($scope, $rootScope, $stateParams, entity, Lignelivraison, Lot, Livraison) {
        var vm = this;
        vm.lignelivraison = entity;
        vm.load = function (id) {
            Lignelivraison.get({id: id}, function(result) {
                vm.lignelivraison = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:lignelivraisonUpdate', function(event, result) {
            vm.lignelivraison = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
