(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BailleurDeleteController',BailleurDeleteController);

    BailleurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bailleur'];

    function BailleurDeleteController($uibModalInstance, entity, Bailleur) {
        var vm = this;
        vm.bailleur = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Bailleur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
