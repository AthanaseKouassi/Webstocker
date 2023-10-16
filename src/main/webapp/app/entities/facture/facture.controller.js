(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FactureController', FactureController);

    FactureController.$inject = ['$scope', '$state', 'Facture', 'FactureSearch'];

    function FactureController ($scope, $state, Facture, FactureSearch) {
        var vm = this;
        vm.factures = [];
        vm.loadAll = function() {
            Facture.query(function(result) {
                vm.factures = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FactureSearch.query({query: vm.searchQuery}, function(result) {
                vm.factures = result;
            });
        };
        vm.loadAll();
        
    }
})();
