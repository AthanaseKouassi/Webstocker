(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ReglementDeleteController',ReglementDeleteController);

    ReglementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reglement'];

    function ReglementDeleteController($uibModalInstance, entity, Reglement) {
        var vm = this;
        vm.reglement = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Reglement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
