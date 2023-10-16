(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieDialogController', CategorieDialogController);

    CategorieDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorie', 'Produit'];

    function CategorieDialogController ($scope, $stateParams, $uibModalInstance, entity, Categorie, Produit) {
        var vm = this;
        vm.categorie = entity;
        vm.produits = Produit.query();
        vm.load = function(id) {
            Categorie.get({id : id}, function(result) {
                vm.categorie = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:categorieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.categorie.id !== null) {
                Categorie.update(vm.categorie, onSaveSuccess, onSaveError);
            } else {
                Categorie.save(vm.categorie, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
