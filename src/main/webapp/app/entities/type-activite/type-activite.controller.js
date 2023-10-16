(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('TypeActiviteController', TypeActiviteController);

    TypeActiviteController.$inject = ['$scope', '$state', 'TypeActivite', 'TypeActiviteSearch'];

    function TypeActiviteController ($scope, $state, TypeActivite, TypeActiviteSearch) {
        var vm = this;
        vm.typeActivites = [];
        vm.loadAll = function() {
            TypeActivite.query(function(result) {
                vm.typeActivites = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TypeActiviteSearch.query({query: vm.searchQuery}, function(result) {
                vm.typeActivites = result;
            });
        };
        vm.loadAll();
        
    }
})();
