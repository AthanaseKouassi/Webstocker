(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LocaliteController', LocaliteController);

    LocaliteController.$inject = ['$scope', '$state', 'Localite', 'LocaliteSearch'];

    function LocaliteController ($scope, $state, Localite, LocaliteSearch) {
        var vm = this;
        vm.localites = [];
        vm.loadAll = function() {
            Localite.query(function(result) {
                vm.localites = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LocaliteSearch.query({query: vm.searchQuery}, function(result) {
                vm.localites = result;
            });
        };
        vm.loadAll();
        
    }
})();
