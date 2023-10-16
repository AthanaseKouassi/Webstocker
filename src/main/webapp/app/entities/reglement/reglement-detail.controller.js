(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ReglementDetailController', ReglementDetailController);

    ReglementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Reglement', 'Facture'];

    function ReglementDetailController($scope, $rootScope, $stateParams, entity, Reglement, Facture) {
        var vm = this;
        vm.reglement = entity;
        vm.load = function (id) {
            Reglement.get({id: id}, function(result) {
                vm.reglement = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:reglementUpdate', function(event, result) {
            vm.reglement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
