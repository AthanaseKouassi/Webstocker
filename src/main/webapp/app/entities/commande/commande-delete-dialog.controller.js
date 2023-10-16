(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommandeDeleteController',CommandeDeleteController);

    CommandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Commande'];

    function CommandeDeleteController($uibModalInstance, entity, Commande) {
        var vm = this;
        vm.commande = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Commande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
