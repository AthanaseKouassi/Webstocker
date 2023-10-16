(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LotDeleteController',LotDeleteController);

    LotDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lot'];

    function LotDeleteController($uibModalInstance, entity, Lot) {
        var vm = this;
        vm.lot = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
