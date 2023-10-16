(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignelivraisonDeleteController',LignelivraisonDeleteController);

    LignelivraisonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lignelivraison'];

    function LignelivraisonDeleteController($uibModalInstance, entity, Lignelivraison) {
        var vm = this;
        vm.lignelivraison = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lignelivraison.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
