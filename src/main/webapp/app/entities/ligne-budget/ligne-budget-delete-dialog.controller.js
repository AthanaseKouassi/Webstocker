(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneBudgetDeleteController',LigneBudgetDeleteController);

    LigneBudgetDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneBudget'];

    function LigneBudgetDeleteController($uibModalInstance, entity, LigneBudget) {
        var vm = this;
        vm.ligneBudget = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LigneBudget.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
