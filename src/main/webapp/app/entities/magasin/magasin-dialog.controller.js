(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MagasinDialogController', MagasinDialogController);

    MagasinDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Magasin', 'Localite', 'Livraison'];

    function MagasinDialogController ($scope, $stateParams, $uibModalInstance, entity, Magasin, Localite, Livraison) {
        var vm = this;
        vm.magasin = entity;
        vm.localites = Localite.query();
        vm.livraisons = Livraison.query();
        vm.load = function(id) {
            Magasin.get({id : id}, function(result) {
                vm.magasin = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:magasinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.magasin.id !== null) {
                Magasin.update(vm.magasin, onSaveSuccess, onSaveError);
            } else {
                Magasin.save(vm.magasin, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
