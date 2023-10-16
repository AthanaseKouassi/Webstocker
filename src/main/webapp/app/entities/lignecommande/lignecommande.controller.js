(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignecommandeController', LignecommandeController);

    LignecommandeController.$inject = ['$scope', '$state', 'Lignecommande', 'LignecommandeSearch'];

    function LignecommandeController ($scope, $state, Lignecommande, LignecommandeSearch) {
        var vm = this;
        vm.lignecommandes = [];
        vm.loadAll = function() {
            Lignecommande.query(function(result) {
                vm.lignecommandes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LignecommandeSearch.query({query: vm.searchQuery}, function(result) {
                vm.lignecommandes = result;
            });
        };
        vm.loadAll();
        
    }
})();
