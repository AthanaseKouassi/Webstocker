(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ObjectifsController', ObjectifsController);

    ObjectifsController.$inject = ['$scope', '$state', 'Objectifs', 'ObjectifsSearch'];

    function ObjectifsController ($scope, $state, Objectifs, ObjectifsSearch) {
        var vm = this;
        vm.objectifs = [];
        vm.loadAll = function() {
            Objectifs.query(function(result) {
                vm.objectifs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ObjectifsSearch.query({query: vm.searchQuery}, function(result) {
                vm.objectifs = result;
            });
        };
        vm.loadAll();
        
    }
})();
