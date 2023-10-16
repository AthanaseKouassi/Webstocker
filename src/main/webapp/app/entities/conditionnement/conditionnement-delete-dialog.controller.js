(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ConditionnementDeleteController',ConditionnementDeleteController);

    ConditionnementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Conditionnement'];

    function ConditionnementDeleteController($uibModalInstance, entity, Conditionnement) {
        var vm = this;
        vm.conditionnement = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Conditionnement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
