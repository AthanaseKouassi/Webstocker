(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BudgetController', BudgetController);

    BudgetController.$inject = ['$scope', '$state', 'Budget', 'BudgetSearch'];

    function BudgetController ($scope, $state, Budget, BudgetSearch) {
        var vm = this;
        vm.budgets = [];
        vm.loadAll = function() {
            Budget.query(function(result) {
                vm.budgets = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BudgetSearch.query({query: vm.searchQuery}, function(result) {
                vm.budgets = result;
            });
        };
        vm.loadAll();
        
    }
})();
