(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('VilleDeleteController',VilleDeleteController);

    VilleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ville'];

    function VilleDeleteController($uibModalInstance, entity, Ville) {
        var vm = this;
        vm.ville = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Ville.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
