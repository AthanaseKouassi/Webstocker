(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ObjectifsDeleteController',ObjectifsDeleteController);

    ObjectifsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Objectifs'];

    function ObjectifsDeleteController($uibModalInstance, entity, Objectifs) {
        var vm = this;
        vm.objectifs = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Objectifs.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
