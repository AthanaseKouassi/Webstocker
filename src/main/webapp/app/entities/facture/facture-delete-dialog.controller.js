(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FactureDeleteController',FactureDeleteController);

    FactureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Facture'];

    function FactureDeleteController($uibModalInstance, entity, Facture) {
        var vm = this;
        vm.facture = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Facture.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
