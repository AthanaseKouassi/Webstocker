(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieclientController', CategorieclientController);

    CategorieclientController.$inject = ['$scope', '$state', 'Categorieclient', 'CategorieclientSearch'];

    function CategorieclientController ($scope, $state, Categorieclient, CategorieclientSearch) {
        var vm = this;
        vm.categorieclients = [];
        vm.loadAll = function() {
            Categorieclient.query(function(result) {
                vm.categorieclients = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CategorieclientSearch.query({query: vm.searchQuery}, function(result) {
                vm.categorieclients = result;
            });
        };
        vm.loadAll();
        
    }
})();
