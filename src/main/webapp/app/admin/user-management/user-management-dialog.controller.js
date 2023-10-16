(function () {
    'use strict';

    angular
            .module('webstockerApp')
            .controller('UserManagementDialogController', UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService'];

    function UserManagementDialogController($stateParams, $uibModalInstance, entity, User, JhiLanguageService) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_CPF',
            'ROLE_CCC',
            'ROLE_MAGASINIER',
            'ROLE_VIH_IST',
            'ROLE_CHEF_ANTENNE',
            'ROLE_LECTURE',
            'ROLE_CONSULTATION',
            'ROLE_GEST_CLIENT',
            'ROLE_ACHAT',
            'ROLE_VENTE',
            'ROLE_ACTIVITE',
            'ROLE_SVP',
            'ROLE_PARAMETRE',
            'ROLE_TCF',
            'ROLE_TRANSFERT',
            'ROLE_OBJECTIF',
            'ROLE_PAP',
            'ROLE_MODIFICATION'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function save() {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
