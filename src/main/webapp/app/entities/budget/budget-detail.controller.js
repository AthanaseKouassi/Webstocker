(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BudgetDetailController', BudgetDetailController);

    BudgetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Budget', 'LigneBudget'];

    function BudgetDetailController($scope, $rootScope, $stateParams, entity, Budget, LigneBudget) {
        var vm = this;
        vm.budget = entity;
        vm.load = function (id) {
            Budget.get({id: id}, function(result) {
                vm.budget = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:budgetUpdate', function(event, result) {
            vm.budget = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
