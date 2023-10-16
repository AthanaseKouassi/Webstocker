(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignefactureController', LignefactureController);

    LignefactureController.$inject = ['$scope', '$state', 'Lignefacture', 'LignefactureSearch'];

    function LignefactureController ($scope, $state, Lignefacture, LignefactureSearch) {
        var vm = this;
        vm.lignefactures = [];
        vm.loadAll = function() {
            Lignefacture.query(function(result) {
                vm.lignefactures = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LignefactureSearch.query({query: vm.searchQuery}, function(result) {
                vm.lignefactures = result;
            });
        };
        vm.loadAll();
        
    }
})();
