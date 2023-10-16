(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneobjectifventeController', LigneobjectifventeController);

    LigneobjectifventeController.$inject = ['$scope', '$state', 'Ligneobjectifvente', 'LigneobjectifventeSearch'];

    function LigneobjectifventeController ($scope, $state, Ligneobjectifvente, LigneobjectifventeSearch) {
        var vm = this;
        vm.ligneobjectifventes = [];
        vm.loadAll = function() {
            Ligneobjectifvente.query(function(result) {
                vm.ligneobjectifventes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LigneobjectifventeSearch.query({query: vm.searchQuery}, function(result) {
                vm.ligneobjectifventes = result;
            });
        };
        vm.loadAll();
        
    }
})();
