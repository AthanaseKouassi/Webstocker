(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ConditionnementDialogController', ConditionnementDialogController);

    ConditionnementDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Conditionnement', 'Produit'];

    function ConditionnementDialogController ($scope, $stateParams, $uibModalInstance, entity, Conditionnement, Produit) {
        var vm = this;
        vm.conditionnement = entity;
        vm.produits = Produit.query();
        vm.load = function(id) {
            Conditionnement.get({id : id}, function(result) {
                vm.conditionnement = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:conditionnementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.conditionnement.id !== null) {
                Conditionnement.update(vm.conditionnement, onSaveSuccess, onSaveError);
            } else {
                Conditionnement.save(vm.conditionnement, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
