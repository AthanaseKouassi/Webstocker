(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('AuteurDeleteController',AuteurDeleteController);

    AuteurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Auteur'];

    function AuteurDeleteController($uibModalInstance, entity, Auteur) {
        var vm = this;
        vm.auteur = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Auteur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
