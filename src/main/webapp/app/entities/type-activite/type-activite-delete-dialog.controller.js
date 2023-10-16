(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('TypeActiviteDeleteController',TypeActiviteDeleteController);

    TypeActiviteDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeActivite'];

    function TypeActiviteDeleteController($uibModalInstance, entity, TypeActivite) {
        var vm = this;
        vm.typeActivite = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TypeActivite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
