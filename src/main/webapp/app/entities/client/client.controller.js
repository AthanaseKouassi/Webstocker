(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ClientController', ClientController);

    ClientController.$inject = ['Client', 'ClientSearch'];

    function ClientController(Client, ClientSearch) {

        var vm = this;

        vm.clients = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Client.query(function(result) {
                vm.clients = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClientSearch.query({query: vm.searchQuery}, function(result) {
                vm.clients = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
