(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('PrixDeleteController',PrixDeleteController);

    PrixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prix'];

    function PrixDeleteController($uibModalInstance, entity, Prix) {
        var vm = this;

        vm.prix = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prix.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
