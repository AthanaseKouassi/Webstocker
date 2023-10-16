(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BudgetDialogController', BudgetDialogController);

    BudgetDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Budget', 'LigneBudget'];

    function BudgetDialogController ($scope, $stateParams, $uibModalInstance, entity, Budget, LigneBudget) {
        var vm = this;
        vm.budget = entity;
        vm.lignebudgets = LigneBudget.query();
        vm.load = function(id) {
            Budget.get({id : id}, function(result) {
                vm.budget = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:budgetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.budget.id !== null) {
                Budget.update(vm.budget, onSaveSuccess, onSaveError);
            } else {
                Budget.save(vm.budget, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
