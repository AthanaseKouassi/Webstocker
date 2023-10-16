(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignelivraisonController', LignelivraisonController);

    LignelivraisonController.$inject = ['$scope', '$state', 'Lignelivraison', 'LignelivraisonSearch'];

    function LignelivraisonController ($scope, $state, Lignelivraison, LignelivraisonSearch) {
        var vm = this;
        vm.lignelivraisons = [];
        vm.loadAll = function() {
            Lignelivraison.query(function(result) {
                vm.lignelivraisons = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LignelivraisonSearch.query({query: vm.searchQuery}, function(result) {
                vm.lignelivraisons = result;
            });
        };
        vm.loadAll();
        
    }
})();
