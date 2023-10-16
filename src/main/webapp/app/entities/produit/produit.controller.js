(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ProduitController', ProduitController);

    ProduitController.$inject = ['$scope', '$state', 'Produit', 'ProduitSearch'];

    function ProduitController ($scope, $state, Produit, ProduitSearch) {
        var vm = this;
        vm.produits = [];
        vm.loadAll = function() {
            Produit.query(function(result) {
                vm.produits = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProduitSearch.query({query: vm.searchQuery}, function(result) {
                vm.produits = result;
            });
        };
        vm.loadAll();
        
    }
})();
