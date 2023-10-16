(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneBonDeSortieDeleteController',LigneBonDeSortieDeleteController);

    LigneBonDeSortieDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneBonDeSortie'];

    function LigneBonDeSortieDeleteController($uibModalInstance, entity, LigneBonDeSortie) {
        var vm = this;
        vm.ligneBonDeSortie = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            LigneBonDeSortie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
