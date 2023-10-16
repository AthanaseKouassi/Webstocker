(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('AuteurController', AuteurController);

    AuteurController.$inject = ['$scope', '$state', 'Auteur', 'AuteurSearch'];

    function AuteurController ($scope, $state, Auteur, AuteurSearch) {
        var vm = this;
        vm.auteurs = [];
        vm.loadAll = function() {
            Auteur.query(function(result) {
                vm.auteurs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AuteurSearch.query({query: vm.searchQuery}, function(result) {
                vm.auteurs = result;
            });
        };
        vm.loadAll();
        
    }
})();
