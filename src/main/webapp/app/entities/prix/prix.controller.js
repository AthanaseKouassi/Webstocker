(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('PrixController', PrixController);

    PrixController.$inject = ['$scope', '$state', 'Prix', 'PrixSearch'];

    function PrixController ($scope, $state, Prix, PrixSearch) {
        var vm = this;

        vm.prixes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Prix.query(function(result) {
                vm.prixes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PrixSearch.query({query: vm.searchQuery}, function(result) {
                vm.prixes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
