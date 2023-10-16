(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FactureDetailController', FactureDetailController);

    FactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Facture', 'Client', 'Reglement', 'BonDeSortie'];

    function FactureDetailController($scope, $rootScope, $stateParams, entity, Facture, Client, Reglement, BonDeSortie) {
        var vm = this;
        vm.facture = entity;
        vm.load = function (id) {
            Facture.get({id: id}, function(result) {
                vm.facture = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:factureUpdate', function(event, result) {
            vm.facture = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
