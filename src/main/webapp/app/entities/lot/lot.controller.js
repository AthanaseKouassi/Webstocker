(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LotController', LotController);

    LotController.$inject = ['$scope', '$state', 'Lot', 'LotSearch'];

    function LotController ($scope, $state, Lot, LotSearch) {
        var vm = this;
        vm.lots = [];
        vm.loadAll = function() {
            Lot.query(function(result) {
                vm.lots = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LotSearch.query({query: vm.searchQuery}, function(result) {
                vm.lots = result;
            });
        };
        vm.loadAll();
        
    }
})();
