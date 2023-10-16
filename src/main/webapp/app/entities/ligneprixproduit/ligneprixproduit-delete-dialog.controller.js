(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneprixproduitDeleteController',LigneprixproduitDeleteController);

    LigneprixproduitDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ligneprixproduit'];

    function LigneprixproduitDeleteController($uibModalInstance, entity, Ligneprixproduit) {
        var vm = this;
        vm.ligneprixproduit = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Ligneprixproduit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
