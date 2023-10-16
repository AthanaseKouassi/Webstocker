(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneobjectifventeDialogController', LigneobjectifventeDialogController);

    LigneobjectifventeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ligneobjectifvente', 'Objectifs', 'Produit'];

    function LigneobjectifventeDialogController ($scope, $stateParams, $uibModalInstance, entity, Ligneobjectifvente, Objectifs, Produit) {
        var vm = this;
        vm.ligneobjectifvente = entity;
        vm.objectifss = Objectifs.query();
        vm.produits = Produit.query();
        vm.load = function(id) {
            Ligneobjectifvente.get({id : id}, function(result) {
                vm.ligneobjectifvente = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:ligneobjectifventeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ligneobjectifvente.id !== null) {
                Ligneobjectifvente.update(vm.ligneobjectifvente, onSaveSuccess, onSaveError);
            } else {
                Ligneobjectifvente.save(vm.ligneobjectifvente, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
