(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieclientDeleteController',CategorieclientDeleteController);

    CategorieclientDeleteController.$inject = ['$uibModalInstance', 'entity', 'Categorieclient'];

    function CategorieclientDeleteController($uibModalInstance, entity, Categorieclient) {
        var vm = this;
        vm.categorieclient = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Categorieclient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
