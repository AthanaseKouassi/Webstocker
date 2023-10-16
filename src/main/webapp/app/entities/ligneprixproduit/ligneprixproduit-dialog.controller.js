(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneprixproduitDialogController', LigneprixproduitDialogController);

    LigneprixproduitDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ligneprixproduit'];

    function LigneprixproduitDialogController ($scope, $stateParams, $uibModalInstance, entity, Ligneprixproduit) {
        var vm = this;
        vm.ligneprixproduit = entity;
        vm.load = function(id) {
            Ligneprixproduit.get({id : id}, function(result) {
                vm.ligneprixproduit = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:ligneprixproduitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ligneprixproduit.id !== null) {
                Ligneprixproduit.update(vm.ligneprixproduit, onSaveSuccess, onSaveError);
            } else {
                Ligneprixproduit.save(vm.ligneprixproduit, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
