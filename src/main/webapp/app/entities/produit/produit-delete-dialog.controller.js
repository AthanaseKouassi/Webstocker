(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ProduitDeleteController',ProduitDeleteController);

    ProduitDeleteController.$inject = ['$uibModalInstance', 'entity', 'Produit'];

    function ProduitDeleteController($uibModalInstance, entity, Produit) {
        var vm = this;
        vm.produit = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Produit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
