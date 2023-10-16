(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MissionDeleteController',MissionDeleteController);

    MissionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mission'];

    function MissionDeleteController($uibModalInstance, entity, Mission) {
        var vm = this;
        vm.mission = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Mission.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
