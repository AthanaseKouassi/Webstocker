(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneBonDeSortieDialogController', LigneBonDeSortieDialogController);

    LigneBonDeSortieDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneBonDeSortie', 'Produit', 'BonDeSortie', 'Lot'];

    function LigneBonDeSortieDialogController ($scope, $stateParams, $uibModalInstance, entity, LigneBonDeSortie, Produit, BonDeSortie, Lot) {
        var vm = this;
        vm.ligneBonDeSortie = entity;
        vm.produits = Produit.query();
        vm.bondesorties = BonDeSortie.query();
        vm.lots = Lot.query();
        vm.load = function(id) {
            LigneBonDeSortie.get({id : id}, function(result) {
                vm.ligneBonDeSortie = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:ligneBonDeSortieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ligneBonDeSortie.id !== null) {
                LigneBonDeSortie.update(vm.ligneBonDeSortie, onSaveSuccess, onSaveError);
            } else {
                LigneBonDeSortie.save(vm.ligneBonDeSortie, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
