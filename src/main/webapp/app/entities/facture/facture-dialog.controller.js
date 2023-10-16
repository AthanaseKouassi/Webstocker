(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FactureDialogController', FactureDialogController);

    FactureDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Facture', 'Client', 'Reglement', 'BonDeSortie'];

    function FactureDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Facture, Client, Reglement, BonDeSortie) {
        var vm = this;
        vm.facture = entity;
        vm.clients = Client.query();
        vm.reglements = Reglement.query();
        vm.bondesorties = BonDeSortie.query({filter: 'facture-is-null'});
        $q.all([vm.facture.$promise, vm.bondesorties.$promise]).then(function() {
            if (!vm.facture.bonDeSortie || !vm.facture.bonDeSortie.id) {
                return $q.reject();
            }
            return BonDeSortie.get({id : vm.facture.bonDeSortie.id}).$promise;
        }).then(function(bonDeSortie) {
            vm.bondesorties.push(bonDeSortie);
        });
        vm.load = function(id) {
            Facture.get({id : id}, function(result) {
                vm.facture = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:factureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.facture.id !== null) {
                Facture.update(vm.facture, onSaveSuccess, onSaveError);
            } else {
                Facture.save(vm.facture, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateFacture = false;
        vm.datePickerOpenStatus.dateLimitePaiement = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
