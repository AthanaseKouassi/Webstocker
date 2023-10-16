(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ReglementController', ReglementController);

    ReglementController.$inject = ['$scope', '$state', 'Reglement', 'ReglementSearch'];

    function ReglementController ($scope, $state, Reglement, ReglementSearch) {
        var vm = this;
        vm.reglements = [];
        vm.loadAll = function() {
            Reglement.query(function(result) {
                vm.reglements = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ReglementSearch.query({query: vm.searchQuery}, function(result) {
                vm.reglements = result;
            });
        };
        vm.loadAll();
        
    }
})();
