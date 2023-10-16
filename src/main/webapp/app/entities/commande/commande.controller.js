(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommandeController', CommandeController);

    CommandeController.$inject = ['$scope', '$state', 'Commande', 'CommandeSearch'];

    function CommandeController ($scope, $state, Commande, CommandeSearch) {
        var vm = this;
        vm.commandes = [];
        vm.loadAll = function() {
            Commande.query(function(result) {
                vm.commandes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CommandeSearch.query({query: vm.searchQuery}, function(result) {
                vm.commandes = result;
            });
        };
        vm.loadAll();
        
    }
})();
