(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BudgetDeleteController',BudgetDeleteController);

    BudgetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Budget'];

    function BudgetDeleteController($uibModalInstance, entity, Budget) {
        var vm = this;
        vm.budget = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Budget.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
