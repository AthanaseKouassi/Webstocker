(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FabricantDetailController', FabricantDetailController);

    FabricantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fabricant', 'Commande', 'Produit'];

    function FabricantDetailController($scope, $rootScope, $stateParams, entity, Fabricant, Commande, Produit) {
        var vm = this;
        vm.fabricant = entity;
        vm.load = function (id) {
            Fabricant.get({id: id}, function(result) {
                vm.fabricant = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:fabricantUpdate', function(event, result) {
            vm.fabricant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
