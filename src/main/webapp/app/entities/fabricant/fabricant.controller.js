(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FabricantController', FabricantController);

    FabricantController.$inject = ['$scope', '$state', 'Fabricant', 'FabricantSearch'];

    function FabricantController ($scope, $state, Fabricant, FabricantSearch) {
        var vm = this;
        vm.fabricants = [];
        vm.loadAll = function() {
            Fabricant.query(function(result) {
                vm.fabricants = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FabricantSearch.query({query: vm.searchQuery}, function(result) {
                vm.fabricants = result;
            });
        };
        vm.loadAll();
        
    }
})();
