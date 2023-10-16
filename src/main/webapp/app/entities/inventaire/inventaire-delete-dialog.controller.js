(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('InventaireDeleteController',InventaireDeleteController);

    InventaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inventaire'];

    function InventaireDeleteController($uibModalInstance, entity, Inventaire) {
        var vm = this;

        vm.inventaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Inventaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
