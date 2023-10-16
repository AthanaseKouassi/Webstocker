(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('RegionDeleteController',RegionDeleteController);

    RegionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Region'];

    function RegionDeleteController($uibModalInstance, entity, Region) {
        var vm = this;
        vm.region = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Region.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
