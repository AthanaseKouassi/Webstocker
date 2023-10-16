(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BailleurController', BailleurController);

    BailleurController.$inject = ['$scope', '$state', 'Bailleur', 'BailleurSearch'];

    function BailleurController ($scope, $state, Bailleur, BailleurSearch) {
        var vm = this;
        vm.bailleurs = [];
        vm.loadAll = function() {
            Bailleur.query(function(result) {
                vm.bailleurs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BailleurSearch.query({query: vm.searchQuery}, function(result) {
                vm.bailleurs = result;
            });
        };
        vm.loadAll();
        
    }
})();
