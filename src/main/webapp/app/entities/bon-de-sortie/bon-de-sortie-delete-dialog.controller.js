(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BonDeSortieDeleteController',BonDeSortieDeleteController);

    BonDeSortieDeleteController.$inject = ['$uibModalInstance', 'entity', 'BonDeSortie'];

    function BonDeSortieDeleteController($uibModalInstance, entity, BonDeSortie) {
        var vm = this;

        vm.bonDeSortie = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BonDeSortie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
