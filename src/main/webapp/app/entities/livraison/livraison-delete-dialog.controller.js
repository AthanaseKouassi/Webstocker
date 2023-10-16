(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LivraisonDeleteController',LivraisonDeleteController);

    LivraisonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Livraison'];

    function LivraisonDeleteController($uibModalInstance, entity, Livraison) {
        var vm = this;
        vm.livraison = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Livraison.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
