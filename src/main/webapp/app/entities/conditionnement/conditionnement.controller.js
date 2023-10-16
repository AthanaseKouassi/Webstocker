(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ConditionnementController', ConditionnementController);

    ConditionnementController.$inject = ['$scope', '$state', 'Conditionnement', 'ConditionnementSearch'];

    function ConditionnementController ($scope, $state, Conditionnement, ConditionnementSearch) {
        var vm = this;
        vm.conditionnements = [];
        vm.loadAll = function() {
            Conditionnement.query(function(result) {
                vm.conditionnements = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ConditionnementSearch.query({query: vm.searchQuery}, function(result) {
                vm.conditionnements = result;
            });
        };
        vm.loadAll();
        
    }
})();
