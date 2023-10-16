(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MagasinController', MagasinController);

    MagasinController.$inject = ['$scope', '$state', 'Magasin', 'MagasinSearch'];

    function MagasinController ($scope, $state, Magasin, MagasinSearch) {
        var vm = this;
        vm.magasins = [];
        vm.loadAll = function() {
            Magasin.query(function(result) {
                vm.magasins = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MagasinSearch.query({query: vm.searchQuery}, function(result) {
                vm.magasins = result;
            });
        };
        vm.loadAll();
        
    }
})();
