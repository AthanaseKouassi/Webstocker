(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieController', CategorieController);

    CategorieController.$inject = ['$scope', '$state', 'Categorie', 'CategorieSearch'];

    function CategorieController ($scope, $state, Categorie, CategorieSearch) {
        var vm = this;
        vm.categories = [];
        vm.loadAll = function() {
            Categorie.query(function(result) {
                vm.categories = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CategorieSearch.query({query: vm.searchQuery}, function(result) {
                vm.categories = result;
            });
        };
        vm.loadAll();
        
    }
})();
