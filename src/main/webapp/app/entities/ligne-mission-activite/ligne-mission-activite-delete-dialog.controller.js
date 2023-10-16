(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneMissionActiviteDeleteController',LigneMissionActiviteDeleteController);

    LigneMissionActiviteDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneMissionActivite'];

    function LigneMissionActiviteDeleteController($uibModalInstance, entity, LigneMissionActivite) {
        var vm = this;
        vm.ligneMissionActivite = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LigneMissionActivite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
