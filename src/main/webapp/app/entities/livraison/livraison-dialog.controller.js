(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LivraisonDialogController', LivraisonDialogController);

    LivraisonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Livraison', 'Magasin', 'Commande', 'Lignelivraison'];

    function LivraisonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Livraison, Magasin, Commande, Lignelivraison) {
        var vm = this;
        vm.livraison = entity;
        vm.magasins = Magasin.query();
        vm.commandes = Commande.query();
        vm.lignelivraisons = Lignelivraison.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:livraisonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.livraison.id !== null) {
                Livraison.update(vm.livraison, onSaveSuccess, onSaveError);
            } else {
                Livraison.save(vm.livraison, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateLivraison = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
