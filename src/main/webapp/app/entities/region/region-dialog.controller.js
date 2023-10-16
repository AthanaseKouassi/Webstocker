(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('RegionDialogController', RegionDialogController);

    RegionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Region'];

    function RegionDialogController ($scope, $stateParams, $uibModalInstance, entity, Region) {
        var vm = this;
        vm.region = entity;
        vm.load = function(id) {
            Region.get({id : id}, function(result) {
                vm.region = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:regionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.region.id !== null) {
                Region.update(vm.region, onSaveSuccess, onSaveError);
            } else {
                Region.save(vm.region, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
