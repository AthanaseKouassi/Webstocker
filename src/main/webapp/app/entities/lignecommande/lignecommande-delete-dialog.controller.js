(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignecommandeDeleteController',LignecommandeDeleteController);

    LignecommandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lignecommande'];

    function LignecommandeDeleteController($uibModalInstance, entity, Lignecommande) {
        var vm = this;
        vm.lignecommande = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lignecommande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
