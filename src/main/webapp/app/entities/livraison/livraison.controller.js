(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LivraisonController', LivraisonController);

    LivraisonController.$inject = ['$scope', '$state', 'Livraison', 'LivraisonSearch'];

    function LivraisonController ($scope, $state, Livraison, LivraisonSearch) {
        var vm = this;
        vm.livraisons = [];
        vm.loadAll = function() {
            Livraison.query(function(result) {
                vm.livraisons = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LivraisonSearch.query({query: vm.searchQuery}, function(result) {
                vm.livraisons = result;
            });
        };
        vm.loadAll();
        
    }
})();
