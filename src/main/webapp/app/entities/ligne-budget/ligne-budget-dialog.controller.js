(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneBudgetDialogController', LigneBudgetDialogController);

    LigneBudgetDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneBudget', 'Budget', 'Mission'];

    function LigneBudgetDialogController ($scope, $stateParams, $uibModalInstance, entity, LigneBudget, Budget, Mission) {
        var vm = this;
        vm.ligneBudget = entity;
        vm.budgets = Budget.query();
        vm.missions = Mission.query();
        vm.load = function(id) {
            LigneBudget.get({id : id}, function(result) {
                vm.ligneBudget = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:ligneBudgetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ligneBudget.id !== null) {
                LigneBudget.update(vm.ligneBudget, onSaveSuccess, onSaveError);
            } else {
                LigneBudget.save(vm.ligneBudget, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
