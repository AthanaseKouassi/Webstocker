(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneprixproduitController', LigneprixproduitController);

    LigneprixproduitController.$inject = ['$scope', '$state', 'Ligneprixproduit', 'LigneprixproduitSearch'];

    function LigneprixproduitController ($scope, $state, Ligneprixproduit, LigneprixproduitSearch) {
        var vm = this;
        vm.ligneprixproduits = [];
        vm.loadAll = function() {
            Ligneprixproduit.query(function(result) {
                vm.ligneprixproduits = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LigneprixproduitSearch.query({query: vm.searchQuery}, function(result) {
                vm.ligneprixproduits = result;
            });
        };
        vm.loadAll();
        
    }
})();
