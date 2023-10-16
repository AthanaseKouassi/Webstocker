(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LocaliteDeleteController',LocaliteDeleteController);

    LocaliteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Localite'];

    function LocaliteDeleteController($uibModalInstance, entity, Localite) {
        var vm = this;
        vm.localite = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Localite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
