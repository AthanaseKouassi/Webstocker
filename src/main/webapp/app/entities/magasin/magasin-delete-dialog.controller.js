(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MagasinDeleteController',MagasinDeleteController);

    MagasinDeleteController.$inject = ['$uibModalInstance', 'entity', 'Magasin'];

    function MagasinDeleteController($uibModalInstance, entity, Magasin) {
        var vm = this;
        vm.magasin = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Magasin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
