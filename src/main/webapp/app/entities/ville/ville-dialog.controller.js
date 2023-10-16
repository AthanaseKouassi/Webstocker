(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('VilleDialogController', VilleDialogController);

    VilleDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ville', 'Region'];

    function VilleDialogController ($scope, $stateParams, $uibModalInstance, entity, Ville, Region) {
        var vm = this;
        vm.ville = entity;
        vm.regions = Region.query();
        vm.load = function(id) {
            Ville.get({id : id}, function(result) {
                vm.ville = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:villeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ville.id !== null) {
                Ville.update(vm.ville, onSaveSuccess, onSaveError);
            } else {
                Ville.save(vm.ville, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
